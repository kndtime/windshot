package com.app.windchat.ui.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.windchat.R;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.adapter.list.StoryAdapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StoryFragment extends Fragment {


    private View root;

    private RecyclerView list;
    private Toolbar toolbar;
    private SwipeRefreshLayout refreshLayout;
    private ProgressBar progressBar;

    private StoryAdapter adapter;

    public StoryFragment() {
        // Required empty public constructor
    }

    public static StoryFragment newInstance(){
        return new StoryFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_story, container, false);
        initViews();
        return root;
    }

    private void initViews(){
        list = (RecyclerView) root.findViewById(R.id.list);
        toolbar = (Toolbar) root.findViewById(R.id.toolbar);
        progressBar = (ProgressBar) root.findViewById(R.id.progressbar);
        refreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh_layout);

        toolbar.setTitle("My Stories");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        adapter = new StoryAdapter(getActivity(), new ArrayList<User>());
        list.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                adapter.clearAll();
                progressBar.setVisibility(View.VISIBLE);
                sendQuerymy();

            }
        });
        sendQuerymy();
    }

    public void sendQuerymy(){
        Call<JsonElement> call = new Api().getRestClient().get_mystory();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    JsonArray array = response.body().getAsJsonArray();
                    for (JsonElement e : array) {
                        Wind wind = new Gson().fromJson(e, Wind.class);
                        if (!adapter.getUsers().get(0).getWinds().contains(wind))
                            adapter.onHeaderChanged(wind);
                    }
                }
                progressBar.setVisibility(View.GONE);
                sendQuery();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }

    private void sendQuery(){
        Call<JsonElement> call = new Api().getRestClient().get_story();
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.isSuccessful()){
                    JsonArray array = response.body().getAsJsonArray();
                    for (JsonElement e : array) {
                        User user = new Gson().fromJson(e, User.class);
                        user.apply();
                        adapter.add(user);
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {

            }
        });
    }
}
