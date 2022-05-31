package com.example.stocktracker.TableFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.stocktracker.Data;
import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {

    TableAdapter adapter;

    private View view;
    private int custUid;

    public TableFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_table, container, false);
        adapter = new TableAdapter(custUid);

        init();
        getData();

        return view;
    }

    private void init() {
        RecyclerView recyclerView = view.findViewById(R.id.table_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectStockList(custUid);
        Log.d("TAG", "" + custUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                Log.d("retrofit", "Find Stock List fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg() + "(selectStockList)");

                    if ("000".equals(data.getResponse_cd())) {
                        adapter.clear();
                        for (Map map : data.getDatas()) {
                            TableItemData itemData = new TableItemData();
                            itemData.setMy_stock_uid(Integer.parseInt(map.get("my_stock_uid").toString()));
                            itemData.setCountry(map.get("country").toString());
                            itemData.setStock_name(map.get("stock_name").toString());
                            itemData.setTicker(map.get("ticker").toString());
                            itemData.setCurrent_price(Integer.parseInt(map.get("close_price").toString()));
                            itemData.setBlended_price(Integer.parseInt(map.get("blended_price").toString()));
                            itemData.setHoldings(Integer.parseInt(map.get("holdings").toString()));
                            itemData.setProfit(Integer.parseInt(map.get("profit").toString()));
                            itemData.setProfit_rate(Float.parseFloat(map.get("profit_rate").toString()));
                            Log.d("TAG", map.get("stock_name").toString());
                            adapter.addItem(itemData);
                        }

                        TextView totalamount = (TextView) view.findViewById(R.id.total_amount);
                        TextView totalprofit = (TextView) view.findViewById(R.id.total_profit);
                        TextView totalprofitrate = (TextView) view.findViewById(R.id.total_profit_rate);
                        totalamount.setText("총평가금액 "+adapter.sumItem()+"원");
                        totalprofit.setText("미실현손익 "+adapter.getTotalProfit()+"원");
                        totalprofitrate.setText("총수익률 "+adapter.getProfitRate()+"%");

                        init();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


}