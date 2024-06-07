package com.example.travelapp;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {
String name,id ;
    public ViewPagerAdapter(@NonNull FragmentManager fm, String name, String id) {
        super(fm);
        this.name = name;
        this.id = id;
        Log.d("place : ", name);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HotelsFragment(name);
            case 1:
                return new FoodsFragment(name);
            case 2:
                return new ActivitiesFragment(name);
            case 3:
                return new PlaceFragment(id, name);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4; // Three tabs
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Hotels";
            case 1:
                return "Foods";
            case 2:
                return "Activities";
            case 3:
                return "About places";
            default:
                return null;
}
}
}

