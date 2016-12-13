package com.app.windchat.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import com.app.windchat.R;
import com.app.windchat.api.model.RestCode;
import com.app.windchat.api.model.User;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.adapter.list.FriendListRecyclerAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendActivity extends AppCompatActivity {


    private EditText search;
    private RecyclerView list;
    private ArrayList<User> users;
    private FriendListRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        initViews();
    }

    private void initViews(){
        search = (EditText) findViewById(R.id.search);
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        users =new ArrayList<>();
        adapter = new FriendListRecyclerAdapter(this, users, false, "TEST");
        list.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.clearAll();
                if (charSequence.length() >= 3)
                    sendSearch(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void sendSearch(String search){
        Call<JsonElement> call = new Api().getRestClient().rawsearch(search);
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
                Toast.makeText(AddFriendActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
