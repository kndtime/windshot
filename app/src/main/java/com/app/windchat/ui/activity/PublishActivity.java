package com.app.windchat.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.Wind;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.fragment.publish.ContactFragment;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishActivity extends AppCompatActivity implements ContactFragment.onGetIdsListener,
View.OnTouchListener{

    File file;
    private ImageView content;
    private ImageView btn_publish, btn_time, btn_who, btn_cancel;
    private EditText btn_text;
    private Wind wind;

    private ViewGroup mRrootLayout;
    private int _xDelta;
    private int _yDelta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        wind = new Wind();
        wind.setDuration(10);
        Intent intent = getIntent();
        file = new File(intent.getStringExtra("path"));
        wind.setImage(Utils.imgTo64(file.getAbsolutePath()));
        initViews();
    }

    private void initViews() {
        btn_cancel = (ImageView) findViewById(R.id.btn_cancel);
        btn_publish = (ImageView) findViewById(R.id.btn_publish);
        btn_time = (ImageView) findViewById(R.id.btn_time);
        btn_who = (ImageView) findViewById(R.id.btn_who);
        btn_text = (EditText) findViewById(R.id.text);
        content = (ImageView) findViewById(R.id.content);

        mRrootLayout = (ViewGroup) findViewById(R.id.root);


        if (file != null)
            Picasso.with(this)
                    .load(file)
                    .fit().centerCrop()
                    .into(content);

        btn_publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContactFragment contactFragment = new ContactFragment();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.activity_publish, contactFragment);
                fragmentTransaction.addToBackStack("contact_fragment");
                fragmentTransaction.commit();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_who.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_who.setEnabled(false);
                Toast.makeText(PublishActivity.this, "Sending your story...", Toast.LENGTH_SHORT).show();
                Call<RestCode> call = new Api().getRestClient().sendStory(wind);
                call.enqueue(new Callback<RestCode>() {
                    @Override
                    public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                        if (response.isSuccessful()) {
                            Utils.startMainIntent();
                            Toast.makeText(PublishActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PublishActivity.this, "Error", Toast.LENGTH_SHORT).show();
                            btn_who.setEnabled(true);
                        }

                    }

                    @Override
                    public void onFailure(Call<RestCode> call, Throwable t) {

                    }
                });
            }
        });

        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_text.getVisibility() == View.GONE) {
                    btn_text.setVisibility(View.VISIBLE);
                } else {
                    btn_text.setVisibility(View.GONE);
                }
            }
        });

        btn_text.setDrawingCacheEnabled(true);
        //btn_text.setOnTouchListener(this);
    }

    public void initFilters() {
        if (btn_text.getText().toString().isEmpty() || Snap.getCurImg() == null) {
            sendImage();
            return;
        }
        int arr[] = new int[2];
        btn_text.getLocationOnScreen(arr);

        Bitmap res = Utils.combineImages(Snap.getCurImg(), btn_text.getDrawingCache(), this, arr);
        if (res != null) {
            wind.setImage(Utils.imgTo64(res));
        }
        sendImage();
    }

    public void sendImage() {
        Call<RestCode> call = new Api().getRestClient().wind_post(wind);
        call.enqueue(new Callback<RestCode>() {
            @Override
            public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                if (response.isSuccessful()) {
                    Utils.startMainIntent();
                    Toast.makeText(PublishActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestCode> call, Throwable t) {

            }
        });
    }

    @Override
    public void onGetIds(ArrayList<Integer> ids) {
        wind.getRecipients().addAll(ids);
        initFilters();
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        final int X = (int) event.getRawX();
        final int Y = (int) event.getRawY();
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                _xDelta = X - lParams.leftMargin;
                _yDelta = Y - lParams.topMargin;
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view
                        .getLayoutParams();
                layoutParams.leftMargin = X - _xDelta;
                layoutParams.topMargin = Y - _yDelta;
                layoutParams.rightMargin = -250;
                layoutParams.bottomMargin = -250;
                view.setLayoutParams(layoutParams);
                break;
        }
        mRrootLayout.invalidate();
        return true;
    }

}
