package com.app.windchat.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;

public class ShowTImeActivity extends AppCompatActivity {

    private ArrayList<Wind> winds;
    private User user;
    private ImageView img;
    private Handler handler;
    private Runnable r;
    private int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time);
        user = Snap.getTmpUser();
        if (user == null)
            finish();
        img = (ImageView) findViewById(R.id.back);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position++;
                handler.removeCallbacks(r);
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
