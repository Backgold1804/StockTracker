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
    private int custUid;

    public ViewpagerFragmentAdapter(FragmentManager fm, int numberOfFragment, int cust_uid) {
        super(fm, numberOfFragment);
        this.numberOfFragment = numberOfFragment;
        this.custUid = cust_uid;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new TableFragment(custUid);
            case 1: return new ChartFragment(custUid);
            default: return new TableFragment(custUid);
        }
    }

    @Override
    public int getCount() {
        return numberOfFragment;
    }

    // 차트를 갱신하기 위해
    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
