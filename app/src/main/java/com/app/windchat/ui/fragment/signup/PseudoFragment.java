package com.app.windchat.ui.fragment.signup;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.app.windchat.R;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnUserCreatedListener} interface
 * to handle interaction events.
 */
public class PseudoFragment extends Fragment {

    private OnUserCreatedListener mListener;
    private View root;
    private Button btn_create;
    private EditText edit_pseudo;
    private static CircularImageView user_img;
    private static File file;

    private static int RESULT_LOAD_IMAGE = 14;
    static final int REQUEST_TAKE_PHOTO = 24;
    private static User current;

    public PseudoFragment() {
        // Required empty public constructor
    }

    public static PseudoFragment newInstance() {

        Bundle args = new Bundle();

        PseudoFragment fragment = new PseudoFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_pseudo, container, false);
        current = new User();
        initViews();
        return root;
    }

    private void initViews(){
        user_img = (CircularImageView) root.findViewById(R.id.user_img);
        btn_create = (Button) root.findViewById(R.id.btn_create);
        edit_pseudo = (EditText) root.findViewById(R.id.user_pseudo);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate())
                    mListener.onCreationInteraction(current);
            }
        });

        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialDialog.Builder(getActivity())
                        .title(R.string.dialog_picture_profile)
                        .items(R.array.img_change)
                        .itemsCallback(new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                switch (which) {
                                    case 0:
                                        Intent i = new Intent(
                                                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        getActivity().startActivityForResult(i, RESULT_LOAD_IMAGE);
                                        break;
                                    case 1:
                                        startCamera();
                                    default:
                                        break;
                                }
                            }
                        })
                        .show();
            }
        });
    }

    private void startCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            getActivity().startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnUserCreatedListener) {
            mListener = (OnUserCreatedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnInfoFilledListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validate(){
        boolean valid = true;
        String pseudo = edit_pseudo.getText().toString();

        if (pseudo.isEmpty()){
            edit_pseudo.setError(Utils.getString(R.string.pseudo_empty));
            return false;
        }

        if (!isAvailable(pseudo)){
            edit_pseudo.setError(Utils.getString(R.string.pseudo_taken));
            return false;
        }
        current.setUsername(pseudo);
        return valid;
    }

    private boolean isAvailable(String pseudo){
        return true;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnUserCreatedListener {
        // TODO: Update argument type and name
        void onCreationInteraction(User user);
    }

    public static void onFragmentResult(int requestCode, int resultCode, Intent data, Context context) {
        Log.d("onFragmentResult", "executed");
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == -1 && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);

            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            file = new File(picturePath);
            Picasso.with(context)
                    .load(selectedImage)
                    .error(R.drawable.windchat)
                    .fit().centerInside()
                    .into(user_img);
            current.setImageStr64(Utils.imgTo64(picturePath));
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == -1 && null != data) {
            Bundle extras = data.getExtras();
            Bitmap bitmap = (Bitmap) extras.get("data");
            try {
                file = Utils.createImageFile();
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                assert bitmap != null;
                bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
                Picasso.with(context)
                        .load(file)
                        .error(R.drawable.windchat)
                        .fit().centerInside()
                        .into(user_img);
                current.setImageStr64(Utils.imgTo64(bitmapdata));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
