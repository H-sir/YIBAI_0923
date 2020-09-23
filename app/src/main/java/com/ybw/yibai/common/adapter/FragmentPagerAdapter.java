package com.ybw.yibai.common.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * @author sjl
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] mFragment;

    public FragmentPagerAdapter(FragmentManager fm, Fragment[] fragment) {
        super(fm);
        mFragment = fragment;
    }

    @Override
    public int getCount() {
        return mFragment == null ? 0 : mFragment.length;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragment[position];
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }
}
