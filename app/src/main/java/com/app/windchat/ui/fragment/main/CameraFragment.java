package com.app.windchat.ui.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.activity.AddFriendActivity;
import com.app.windchat.ui.activity.FriendActivity;
import com.app.windchat.ui.activity.MainActivity;
import com.app.windchat.ui.view.SwipeGestureDetector;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.CameraView;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CameraFragment extends Fragment implements CameraHostProvider {


    private View root;
    private CameraView cameraView;
    private ImageView ivTakenPhoto;
    private CircularImageView sl_img;
    private Button btnTakePhoto;
    private ImageView btnFlash, btnProfile, btnSwap, btnWind, btnStory;
    private TextView sl_name, sl_username, sl_add_friend, sl_added_me;
    private SlidingUpPanelLayout slidingUpPanel;
    private User current;

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance(){
        return new CameraFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root =  inflater.inflate(R.layout.fragment_camera, container, false);
        current = Snap.getCurrent();
        initViews();
        return root;
    }

    private void initViews(){
        btnTakePhoto = (Button) root.findViewById(R.id.btnTakePhoto);
        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTakePhotoClick();
            }
        });
        cameraView = (CameraView) root.findViewById(R.id.cameraView);
        cameraView.autoFocus();
        cameraView.lockToLandscape(false);
        btnWind = (ImageView) root.findViewById(R.id.btn_wind);
        btnStory = (ImageView) root.findViewById(R.id.btn_story);
        btnFlash = (ImageView) root.findViewById(R.id.btn_flash);
        btnProfile = (ImageView) root.findViewById(R.id.btn_profile);
        btnSwap = (ImageView) root.findViewById(R.id.btn_swap);
        sl_img = (CircularImageView) root.findViewById(R.id.img);
        sl_name = (TextView) root.findViewById(R.id.name);
        sl_username = (TextView) root.findViewById(R.id.username);
        sl_add_friend = (TextView) root.findViewById(R.id.add_friend);
        sl_added_me = (TextView) root.findViewById(R.id.added_me);
        slidingUpPanel = (SlidingUpPanelLayout) root.findViewById(R.id.sliding_layout);

        fetchUser();

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slidingUpPanel.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
        btnWind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getPager().setCurrentItem(0);
            }
        });
        btnStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.getPager().setCurrentItem(2);
            }
        });
        btnFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cameraView.getFlashMode() == null)
                    return;
                if (cameraView.getFlashMode().equals("off")) {
                    setFlashMode("on");
                    btnFlash.setImageResource(R.drawable.flash_on_24dp);
                }
                else {
                    setFlashMode("off");
                    btnFlash.setImageResource(R.drawable.flash_off_24dp);
                }
            }
        });

        btnSwap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swapCamera();
            }
        });

        cameraView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gd.onTouchEvent(event);
            }
        });

    }

    public void startFaceDetection() {
        cameraView.startFaceDetection();
    }

    public void stopFaceDetection() {
        cameraView.stopFaceDetection();
    }

    public void setFlashMode(String mode) {
        cameraView.setFlashMode(mode);
    }

    public void swapCamera(){
        cameraView.onPause();
        MainActivity.toggle();
        cameraView.onResume();
    }

    private void setProfile(){
        Picasso.with(getActivity())
                .load(current.getPictureUrl())
                .fit().centerCrop()
                .into(sl_img);
        sl_name.setText(current.getCompleteName());
        sl_username.setText(current.getUsername());

        sl_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FriendActivity.class);
                getActivity().startActivity(i);
            }
        });

        sl_added_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), FriendActivity.class);
                i.putExtra("friend", false);
                getActivity().startActivity(i);
            }
        });

        sl_add_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), AddFriendActivity.class);
                i.putExtra("friend", false);
                getActivity().startActivity(i);
            }
        });
    }

    private void fetchUser(){
        Call<User> call = new Api().getRestClient().get_profile();
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    current = response.body();
                }
                setProfile();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        btnTakePhoto.setEnabled(true);
        cameraView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    public void onTakePhotoClick() {
        btnTakePhoto.setEnabled(false);
        cameraView.animate();
        cameraView.takePicture(true, true);
    }

    @Override
    public CameraHost getCameraHost() {
        return new MainActivity().getCameraHost();
    }

    final GestureDetector gd = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            swapCamera();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
            cameraView.autoFocus();
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

    });
}
