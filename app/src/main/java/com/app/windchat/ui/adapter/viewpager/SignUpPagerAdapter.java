package com.app.windchat.ui.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.app.windchat.ui.fragment.signup.InfoFragment;
import com.app.windchat.ui.fragment.signup.PseudoFragment;


/**
 * Created by banal_a on 07/12/2016.
 */

public class SignUpPagerAdapter extends FragmentPagerAdapter {

    private static int NUM_ITEM = 2;
    private FragmentManager fm;

    public SignUpPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fm = fm;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case  0:
                return InfoFragment.newInstance();
            case  1:
                return PseudoFragment.newInstance();
            default:
                break;
        }
        return InfoFragment.newInstance();
    }

    @Override
    public int getCount() {
        return NUM_ITEM;
    }
}