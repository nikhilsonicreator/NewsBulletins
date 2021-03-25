package com.entwickler.newsbulletins;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {

    public TabAdapter(FragmentManager fm)
    {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                SearchFragment profileTab=new SearchFragment();
                return profileTab;
            case 1:
                return new LiveNewsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Search";
            case 1:
                return "Bullets";
            default:
                return null;
        }
    }
}
