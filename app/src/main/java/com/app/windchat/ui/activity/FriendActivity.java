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
        Call<ArrayList<User>> call = new Api().getRestClient().get_friends();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()){
                    adapter.addAll(response.body());
                } else {
                    Toast.makeText(FriendActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(FriendActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void friend(){
        Call<ArrayList<User>> call = new Api().getRestClient().get_pendingList();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()){
                    adapter.addAll(response.body());
                } else {
                    Toast.makeText(FriendActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Toast.makeText(FriendActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
