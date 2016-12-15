package com.app.windchat.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.ui.adapter.viewpager.MainPagerAdapter;
import com.commonsware.cwac.camera.CameraHost;
import com.commonsware.cwac.camera.CameraHostProvider;
import com.commonsware.cwac.camera.PictureTransaction;
import com.commonsware.cwac.camera.SimpleCameraHost;

import java.io.File;

/**
 * Created by banal_a on 07/12/2016.
 */

public class MainActivity extends AppCompatActivity implements CameraHostProvider {

    private static ViewPager pager;
    File photoPath;
    public static boolean isFacing = true;
    public static MyCameraHost cameraHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraHost = new MyCameraHost(this);
        initViews();
    }

    private void initViews(){
        pager = (ViewPager) findViewById(R.id.pager);
        MainPagerAdapter mainAdapter = new MainPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mainAdapter);
        pager.setCurrentItem(1);
    }

    public static void toggle(){
        cameraHost.toggle();
    }

    public static ViewPager getPager() {
        return pager;
    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() != 1)
            pager.setCurrentItem(1);
        else
            super.onBackPressed();
    }

    @Override
    public CameraHost getCameraHost() {
        return cameraHost;
    }

    class MyCameraHost extends SimpleCameraHost {

        private Camera.Size previewSize;
        private int camID = Camera.CameraInfo.CAMERA_FACING_FRONT;

        public MyCameraHost(Context ctxt) {
            super(ctxt);
        }

        @Override
        public boolean useFullBleedPreview() {
            return true;
        }

        @Override
        public Camera.Size getPictureSize(PictureTransaction xact, Camera.Parameters parameters) {
            return previewSize;
        }

        @Override
        public Camera.Parameters adjustPreviewParameters(Camera.Parameters parameters) {
            Camera.Parameters parameters1 = super.adjustPreviewParameters(parameters);
            previewSize = parameters1.getPreviewSize();
            return parameters1;
        }

        @Override
        public void saveImage(PictureTransaction xact, final Bitmap bitmap) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    photoPath = getPhotoPath();
                    showTakenPicture(bitmap);
                    Snap.setCurImg(bitmap);
                }
            });
        }

        @Override
        public int getCameraId() {
            return camID;
        }

        private void toggle() {
            if(camID == Camera.CameraInfo.CAMERA_FACING_BACK){
                camID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            }
            else {
                camID = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
        }
    }

    private void showTakenPicture(Bitmap bitmap) {
        Snap.setCurImg(bitmap);
        Intent intent = new Intent(this, PublishActivity.class);
        intent.putExtra("path", photoPath.getAbsolutePath());
        startActivity(intent);
    }
}
