package com.app.windchat.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.app.windchat.R;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.adapter.list.FriendListRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendActivity extends AppCompatActivity {

    RecyclerView list;
    Toolbar toolbar;
    ArrayList<User> users;
    boolean isFriend;
    FriendListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
        Intent i = getIntent();
        isFriend = i.getBooleanExtra("friend", true);
        users = new ArrayList<>();
        initViews();
    }

    private void initViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        list = (RecyclerView) findViewById(R.id.list);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        list.setLayoutManager(llm);
        adapter = new FriendListRecyclerAdapter(
                getApplication(),
                users,
                isFriend);
        list.setAdapter(adapter);
        if (isFriend)
            request();
        else
            friend();
    }

    private void request(){
        Call<JsonElement> call  = new Api().getRestClient().get_rawfriends();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    JsonArray array = response.body().getAsJsonArray();
                    if (array!=null) {
                        for (JsonElement e : array) {
                            adapter.add(new Gson().fromJson(e, User.class));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void friend(){
        Call<JsonElement> call  = new Api().getRestClient().get_rawpendingList();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    JsonArray array = response.body().getAsJsonArray();
                    if (array!=null) {
                        for (JsonElement e : array) {
                            adapter.add(new Gson().fromJson(e, User.class));
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
}
