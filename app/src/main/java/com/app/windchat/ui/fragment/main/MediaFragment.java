package com.app.windchat.ui.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.app.windchat.R;
import com.app.windchat.Snap;
import com.app.windchat.api.model.User;
import com.app.windchat.api.model.Wind;
import com.app.windchat.api.rest.Api;
import com.app.windchat.ui.adapter.list.WindRecyclerAdapter;

import java.io.File;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MediaFragment extends Fragment {

    private View root;
    private User current;

    private File photoPath;
    private RecyclerView list;

    public MediaFragment() {
        // Required empty public constructor
    }

    public static MediaFragment newInstance(){
        return new MediaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        current = Snap.getCurrent();
        root = inflater.inflate(R.layout.fragment_media, container, false);
        list = (RecyclerView) root.findViewById(R.id.list);
        RecyclerView.LayoutManager llm = new LinearLayoutManager(getActivity());
        list.setLayoutManager(llm);
        list.setItemAnimator(new DefaultItemAnimator());
        final ArrayList<Wind> winds = new ArrayList<>();
        final WindRecyclerAdapter adapter = new WindRecyclerAdapter(getContext(), winds);
        list.setAdapter(adapter);

        Call<ArrayList<User>> call =  new Api().getRestClient().get_winds();
        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                if (response.isSuccessful()){
                    winds.clear();
                    for (User user : response.body()) {
                        adapter.addAll(user.getWinds());
                    }

                }else {
                    Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {

            }
        });
        return root;
    }

}
