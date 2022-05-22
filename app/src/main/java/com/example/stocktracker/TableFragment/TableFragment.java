package com.example.stocktracker.TableFragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stocktracker.R;
import com.example.stocktracker.databinding.FragmentTableBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TableFragment extends Fragment {

    TableAdapter adapter;

    FragmentTableBinding binding;
    ArrayList<TableItemData> TableItemData;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TableFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TableFragment newInstance(int param1) {
        TableFragment fragment = new TableFragment();
        Bundle args = new Bundle();
        args.putInt("number", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_table, container, false);
        init(view);
        getData();

        // 상단 Total 정보
        TextView totalamount = (TextView) view.findViewById(R.id.total_amount);
        TextView totalprofit = (TextView) view.findViewById(R.id.total_profit);
        TextView totalprofitrate = (TextView) view.findViewById(R.id.total_profit_rate);
        totalamount.setText("총평가금액 "+adapter.sumItem()+"원");
        totalprofit.setText("미실현손익 "+adapter.getTotalProfit()+"원");
        totalprofitrate.setText("총수익률 "+adapter.getProfitRate()+"%");

        return view;
    }

    private void init(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.TableRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dvItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dvItemDecoration);

        adapter = new TableAdapter();

        recyclerView.setAdapter(adapter);
    }

    //  adapter안에 데이터를 넣어줌
    private void getData() {
        TableItemData tableItemData = new TableItemData("KOR", "aaa", "123",10100, 10000, 10000, 5, 100,1.0f, 3.3f, 20220511, "매매", 10000);
        adapter.addItem(tableItemData);
        tableItemData = new TableItemData("US", "bbb", "456",20400, 20000, 20000, 10, 400, 2.0f, 12.0f, 20220510, "매매", 20000);
        adapter.addItem(tableItemData);
        tableItemData = new TableItemData("UK", "ccc", "789",30900, 30000, 30000, 15, 900,3.0f, 30.0f, 20220509, "매매", 30000);
        adapter.addItem(tableItemData);
        tableItemData = new TableItemData("JPN", "ddd", "012",41200, 40000, 40000, 20, 1200, 4.0f, 54.7f, 20220508, "매매", 40000);
        adapter.addItem(tableItemData);
    }
}