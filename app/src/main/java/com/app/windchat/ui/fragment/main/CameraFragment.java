package com.app.windchat.ui.fragment.main;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.windchat.R;
import com.app.windchat.ui.activity.MainActivity;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.CameraView;


public class CameraFragment extends Fragment implements CameraHostProvider {


    private View root;
    private CameraView cameraView;
    private ImageView ivTakenPhoto;
    private Button btnTakePhoto;
    private ImageView btnFlash, btnProfile, btnSwap, btnWind, btnStory;

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
        btnWind = (ImageView) root.findViewById(R.id.btn_wind);
        btnStory = (ImageView) root.findViewById(R.id.btn_story);
        btnFlash = (ImageView) root.findViewById(R.id.btn_flash);
        btnProfile = (ImageView) root.findViewById(R.id.btn_profile);
        btnSwap = (ImageView) root.findViewById(R.id.btn_swap);

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

    @Override
    public void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraView.onPause();
    }

    public void onTakePhotoClick() {
        btnTakePhoto.setEnabled(false);
        cameraView.takePicture(true, true);
    }

    @Override
    public CameraHost getCameraHost() {
        return new MainActivity().getCameraHost();
    }

    final GestureDetector gd = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener(){


        //here is the method for double tap


        @Override
        public boolean onDoubleTap(MotionEvent e) {
            swapCamera();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);

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
