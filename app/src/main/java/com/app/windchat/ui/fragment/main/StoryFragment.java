package com.app.windchat.ui.fragment.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.windchat.R;


public class StoryFragment extends Fragment {


    private View root;

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
    }
}
