package com.example.stocktracker.TableFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;

public class ViewpagerFragmentAdapter extends FragmentStatePagerAdapter {

    private int numberOfFragment;

    public ViewpagerFragmentAdapter(FragmentManager fm, int numberOfFragment) {
        super(fm, numberOfFragment);
        this.numberOfFragment = numberOfFragment;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new TableFragment();
            case 1: return new ChartFragment();
            default: return new TableFragment();
        }
    }


    @Override
    public int getCount() {
        return numberOfFragment;
    }
}
