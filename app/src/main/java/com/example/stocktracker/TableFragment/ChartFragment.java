package com.example.stocktracker.TableFragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChartFragment extends Fragment {

    private ArrayList<ChartData> chartData = new ArrayList<ChartData>();
    private int custUid;
    private View view;

    private List<PieEntry> entries;

    public ChartFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);
        getTestData();
//        getData();

        calcHoldingWeight();

        PieChart pieChart = view.findViewById(R.id.PieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        entries = new ArrayList<>();

        for (int i = 0; i < chartData.size(); i++) {
            entries.add(new PieEntry(chartData.get(i).getChart_holding_weight(), chartData.get(i).getChart_stock_name()));
        }

        PieDataSet set = new PieDataSet(entries, "보유비중");
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        PieData data = new PieData(set);
        data.setValueFormatter(new PercentFormatter());
        pieChart.setData(data);
        pieChart.invalidate();

        return view;
    }

    // 보유 비중 계산
    private void calcHoldingWeight() {
        int sum = 0;
        for (int i = 0; i < chartData.size(); i++) {
            sum += chartData.get(i).getChart_holdings() * chartData.get(i).getChart_current_price();
        }

        for (int i = 0; i < chartData.size(); i++) {
            chartData.get(i).setChart_holding_weight((float) chartData.get(i).getChart_holdings() * chartData.get(i).getChart_current_price() / sum);
        }
    }

    // Test Data
    private void getTestData() {
        ChartData temp = new ChartData();
        temp.setChart_holdings(10);
        temp.setChart_current_price(50000);
        temp.setChart_stock_name("삼성전자");
        temp.setChart_profit_rate(0.1f);
        chartData.add(temp);
        temp = new ChartData();
        temp.setChart_holdings(5);
        temp.setChart_current_price(30000);
        temp.setChart_stock_name("LG전자");
        temp.setChart_profit_rate(0.04f);
        chartData.add(temp);
        temp = new ChartData();
        temp.setChart_holdings(7);
        temp.setChart_current_price(83000);
        temp.setChart_stock_name("카카오");
        temp.setChart_profit_rate(0.08f);
        chartData.add(temp);
    }

    // Data 불러오기
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
                        for (Map map : data.getDatas()) {
                            ChartData itemData = new ChartData();
                            itemData.setChart_stock_name(map.get("stock_name").toString());
                            itemData.setChart_current_price(Integer.parseInt(map.get("close_price").toString()));
                            itemData.setChart_holdings(Integer.parseInt(map.get("holdings").toString()));
                            itemData.setChart_profit_rate(Float.parseFloat(map.get("profit_rate").toString()));
                            Log.d("TAG", map.get("stock_name").toString());
                            chartData.add(itemData);
                        }
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