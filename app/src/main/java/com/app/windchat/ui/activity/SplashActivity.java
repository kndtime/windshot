package com.app.windchat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                if (Snap.getCurrent().getToken().isEmpty()){
                Intent intent = new Intent(getApplication(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                }
                else{
                    Utils.startMainIntent();
                }
            }
        };
        handler.postDelayed(r, 2000);
    }
}
