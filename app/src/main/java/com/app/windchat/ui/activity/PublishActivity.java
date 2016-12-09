package com.app.windchat.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.windchat.R;
import com.app.windchat.Utils;
import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.Wind;
import com.app.windchat.api.rest.Api;
import com.squareup.picasso.Picasso;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishActivity extends AppCompatActivity {

    File file;
    private ImageView content;
    private Wind wind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        wind = new Wind();
        wind.setDuration(10);
        wind.getRecipients().add(77);
        wind.getRecipients().add(3);
        Intent intent = getIntent();
        file = new File(intent.getStringExtra("path"));
        wind.setImage(Utils.imgTo64(file.getAbsolutePath()));
        sendImage();
        initViews();
    }

    private void initViews(){
        content = (ImageView) findViewById(R.id.content);
        if (file != null)
            Picasso.with(this).load(file).fit().centerInside().into(content);
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
}
