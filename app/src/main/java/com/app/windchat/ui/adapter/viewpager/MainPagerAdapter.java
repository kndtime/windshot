package com.app.windchat.ui.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.windchat.ui.fragment.main.CameraFragment;
import com.app.windchat.ui.fragment.main.MediaFragment;
import com.app.windchat.ui.fragment.main.StoryFragment;

/**
 * Created by banal_a on 05/12/2016.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEM = 3;
    private FragmentManager fm;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case  0:
                return MediaFragment.newInstance();
            case  1:
                return CameraFragment.newInstance();
            case  2:
                return StoryFragment.newInstance();
            default:
                break;
        }
        return CameraFragment.newInstance();
    }

    @Override
    public int getCount() {
        return NUM_ITEM;
    }
}
