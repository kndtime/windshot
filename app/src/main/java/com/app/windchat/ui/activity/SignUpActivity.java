package com.app.windchat.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;


import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.Utils;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.adapter.viewpager.SignUpPagerAdapter;
import com.app.windchat.ui.fragment.signup.InfoFragment;
import com.app.windchat.ui.fragment.signup.PseudoFragment;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity implements InfoFragment.OnInfoFilledListener,
        PseudoFragment.OnUserCreatedListener {

    private User current;
    private ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
    }

    private void initViews(){
        pager = (ViewPager) findViewById(R.id.pager);
        SignUpPagerAdapter mainAdapter = new SignUpPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(mainAdapter);
        pager.setCurrentItem(0);
    }

    @Override
    public void onFragmentInteraction(User user) {
        current = user;
        pager.setCurrentItem(1);
        Toast.makeText(this, "Data received..", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreationInteraction(User user) {
        current.setPictureUrl(user.getPictureUrl());
        current.setUsername(user.getUsername());
        current.setImageStr64(user.getImageStr64());
        Toast.makeText(this, "User created..", Toast.LENGTH_SHORT).show();
        Api api = new Api();

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", current.getEmail());
        jsonObject.addProperty("birthday", current.getBirthday());
        jsonObject.addProperty("password", current.getPassword());
        jsonObject.addProperty("userName", current.getUsername());
        jsonObject.addProperty("firstName", current.getFirstname());
        jsonObject.addProperty("lastName", current.getLastname());
        jsonObject.addProperty("imageStr64", current.getImageStr64());

        Call<User> call = api.getRestClient().register(jsonObject);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()){
                    Utils.startMainIntent();
                    Snap.setCurrent(response.body());
                }else{
                    Toast.makeText(SignUpActivity.this, "Error.." + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        if (pager.getCurrentItem() > 0)
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        else
            super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        PseudoFragment.onFragmentResult(requestCode, resultCode, data, this);

    }


}
