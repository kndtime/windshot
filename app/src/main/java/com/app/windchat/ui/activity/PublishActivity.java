package com.app.windchat.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.windchat.R;
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

public class PublishActivity extends AppCompatActivity implements ContactFragment.onGetIdsListener {

    File file;
    private ImageView content;
    private ImageView btn_publish, btn_time, btn_who, btn_cancel;
    private Wind wind;

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

    private void initViews(){
        btn_cancel = (ImageView) findViewById(R.id.btn_cancel);
        btn_publish = (ImageView) findViewById(R.id.btn_publish);
        btn_time = (ImageView) findViewById(R.id.btn_time);
        btn_who = (ImageView) findViewById(R.id.btn_who);
        content = (ImageView) findViewById(R.id.content);
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

            }
        });
    }

    public void sendImage(){
        Call<RestCode> call = new Api().getRestClient().wind_post(wind);
        call.enqueue(new Callback<RestCode>() {
            @Override
            public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                if (response.isSuccessful()){
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
        sendImage();
    }
}
