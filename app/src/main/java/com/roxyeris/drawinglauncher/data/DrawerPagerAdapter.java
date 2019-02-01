package com.roxyeris.drawinglauncher.data;

import android.content.pm.ApplicationInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.roxyeris.drawinglauncher.DrawerFragment;
import com.roxyeris.drawinglauncher.category.AppCategory;
import com.roxyeris.drawinglauncher.category.CategoryManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by arinpc on 2017-01-24.
 */

public class DrawerPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragmentList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();

    public DrawerPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void addNewFragment(Fragment frag, String title) {
        fragmentList.add(frag);
        titleList.add(title);
    }

    public void clearFragment() {
        fragmentList.clear();
        titleList.clear();
    }
}
