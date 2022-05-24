package com.example.stocktracker.TableFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.stocktracker.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;


public class TableSetFragment extends Fragment {

    private TableFragment tableFragment;
    private ChartFragment chartFragment;
    ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewpagerFragmentAdapter viewpagerFragmentAdapter;
    private int tabCurrentIdx = 0;
    private int custUid;

    public TableSetFragment(int cust_uid) {
        this.custUid = cust_uid;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_table_set, container, false);

        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.viewPager);
        //피드 구성하는 탭레이아웃 + 뷰페이저
        //커스텀 어댑터 생성
        viewpagerFragmentAdapter = new ViewpagerFragmentAdapter(getChildFragmentManager(), 2, custUid);
        viewPager.setAdapter(viewpagerFragmentAdapter);
        viewPager.setCurrentItem(tabCurrentIdx);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                tabCurrentIdx = tab.getPosition();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

}