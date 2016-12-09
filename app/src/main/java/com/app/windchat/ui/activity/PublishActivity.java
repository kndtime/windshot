package com.app.windchat.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.app.windchat.R;
import com.squareup.picasso.Picasso;

import java.io.File;

public class PublishActivity extends AppCompatActivity {

    File file;
    private ImageView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        Intent intent = getIntent();
        //bitmap = (Bitmap) intent.getParcelableExtra("BitmapImage");
        file = new File(intent.getStringExtra("path"));
        initViews();
    }

    private void initViews(){
        content = (ImageView) findViewById(R.id.content);
        if (file != null)
            Picasso.with(this).load(file).fit().centerCrop().into(content);
    }
}
