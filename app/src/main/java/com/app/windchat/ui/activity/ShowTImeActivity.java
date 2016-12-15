package com.app.windchat.ui.activity;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.app.windchat.api.rest.Api;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowTImeActivity extends AppCompatActivity {

    private ArrayList<Wind> winds;
    private User user;
    private ImageView img;
    private TextView count;
    private Handler handler;
    private Runnable r;
    private Runnable tmprun;
    private Handler hand;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time);
        user = Snap.getTmpUser();
        if (user == null)
            finish();
        img = (ImageView) findViewById(R.id.back);
        count = (TextView) findViewById(R.id.count);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                handler.removeCallbacks(r);
                hand.removeCallbacks(tmprun);
                displayImage();
            }
        });
        winds  = user.getWinds();
        handler = new Handler();
        displayImage();
    }


    public void displayImage(){
        if (winds.size() == position){
            Utils.startMainIntent();
        }
        else
        {
            if (getItem().isOpened())
            {
                position++;
                displayImage();
                return;
            }
            Call<RestCode> call = new Api().getRestClient().viewing(getItem().getId());
            call.enqueue(new Callback<RestCode>() {
                @Override
                public void onResponse(Call<RestCode> call, Response<RestCode> response) {
                    if (response.isSuccessful()){

                    }
                }

                @Override
                public void onFailure(Call<RestCode> call, Throwable t) {

                }
            });

            new CountDownTimer((getItem().getDuration() + 1) * 1000, 1000) {

                public void onTick(long millisUntilFinished) {
                    String tmp = String.format("%02d", millisUntilFinished / 1000);
                    count.setText(tmp);
                }

                public void onFinish() {
                    String tmp = String.format("%02d", 0);
                    count.setText(tmp);
                }

            }.start();

        Picasso.with(this)
                .load(getItem().getImageUrl())
                .fit().centerCrop()
                .into(img);
        r = new Runnable() {
            @Override
            public void run() {
                position ++;
                displayImage();
            }
        };
        handler.postDelayed(r, getItem().getDuration() * 1000);
        }
    }

    private Wind getItem(){
        return winds.get(position);
    }
}
