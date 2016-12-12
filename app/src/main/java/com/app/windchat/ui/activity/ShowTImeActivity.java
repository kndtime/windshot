package com.app.windchat.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.app.windchat.R;
import com.app.windchat.api.model.Wind;

import java.util.ArrayList;

public class ShowTImeActivity extends AppCompatActivity {

    private ArrayList<Wind> winds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_time);
        Intent i = getIntent();
        winds  = new ArrayList<>();
        winds =  i.getParcelableExtra("winds");
    }
}
