package com.example.stocktracker.TableFragment;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.stocktracker.ListData;
import com.example.stocktracker.MainActivity;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
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
    private int sum;
    private PieChart pieChart;
    private ChartAdapter adapter;


    private List<PieEntry> entries;

    public ChartFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);

        getData();

        return view;
    }

    private void drawPieChart() {
        pieChart = view.findViewById(R.id.PieChart);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("보유비중(%)\n"+new DecimalFormat("총 ###,###,###원").format(sum));
        entries = new ArrayList<>();

        for (int i = 0; i < chartData.size(); i++) {
            entries.add(new PieEntry(chartData.get(i).getChart_holding_weight(), chartData.get(i).getChart_stock_name()));
        }

        if (entries.size() > 0) {
            // outside label
            pieChart.setMinAngleForSlices(20f);

            // legend
            Legend legend = pieChart.getLegend();
            legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
            legend.setOrientation(Legend.LegendOrientation.VERTICAL);
            legend.setDrawInside(false);
            legend.setEnabled(false);

            // 차트 데이터 설정
            PieDataSet set = new PieDataSet(entries, "");
            set.setColors(ColorTemplate.JOYFUL_COLORS);

            // outside label
            set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            set.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

            // 컬러 리스트 생성
            ArrayList<Integer> colors = new ArrayList<Integer>();
            for (int i = 0; i < ColorTemplate.JOYFUL_COLORS.length; i++) {
                colors.add(ColorTemplate.JOYFUL_COLORS[i]);
            }

            // 데이터 속성 설정
            PieData data = new PieData(set);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(12f);
            data.setValueTextColors(colors);
            set.setUsingSliceColorAsValueLineColor(true);

            pieChart.setData(data);
            pieChart.invalidate();

            initLegend();
        } else {
            // 보유중인 종목이 없을 때
            pieChart.setNoDataText("보유중인 종목이 없습니다.");
            pieChart.setNoDataTextColor(Color.BLACK);
            pieChart.invalidate();
        }

        // Selected Data Dialog
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                DecimalFormat df = new DecimalFormat("###,###,###");
                int x = pieChart.getData().getDataSetForEntry(e).getEntryIndex((PieEntry) e);

                String name = chartData.get(x).getChart_stock_name();
                String holdings = df.format(chartData.get(x).getChart_holdings()) + "주";
                String current = df.format(chartData.get(x).getChart_current_price()) + "원";
                String price = df.format(chartData.get(x).getChart_holdings() * chartData.get(x).getChart_current_price()) + "원";

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setCancelable(true);
                View v = LayoutInflater.from(view.getContext()).inflate(R.layout.piechart_dialog, null);
                TextView stock_name = v.findViewById(R.id.chart_stock_name);
                stock_name.setTextColor(ColorTemplate.JOYFUL_COLORS[x]);
                TextView amount_price = v.findViewById(R.id.chart_amount_price);
                TextView stock_holdings = v.findViewById(R.id.chart_holdings);
                TextView stock_current_price = v.findViewById(R.id.chart_current_price);
                stock_name.setText(name);
                stock_holdings.setText(holdings);
                stock_current_price.setText(current);
                amount_price.setText(price);
                builder.setView(v)
                        .create()
                        .show();
            }

            @Override
            public void onNothingSelected() {

            }
        });
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
        this.sum = sum;
    }

    // Data 불러오기
    private void getData() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectChartList(custUid);
        Log.d("TAG", "" + custUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                Log.d("retrofit", "Select Chart List fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg() + "(selectStockList)");

                    if ("000".equals(data.getResponse_cd())) {
                        for (Map map : data.getDatas()) {
                            ChartData itemData = new ChartData();
                            itemData.setChart_stock_name(map.get("stock_name").toString());
                            itemData.setChart_current_price(Integer.parseInt(map.get("close_price").toString()));
                            itemData.setChart_holdings(Integer.parseInt(map.get("holdings").toString()));
                            Log.d("TAG", map.get("stock_name").toString());
                            chartData.add(itemData);
                        }

                        calcHoldingWeight();

                        drawPieChart();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    // Legend Recyclerview Setting
    public void initLegend() {
        RecyclerView recyclerView = view.findViewById(R.id.chart_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        //  종목에 구분을 위하여 구분선을 추가
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        adapter = new ChartAdapter(chartData, ColorTemplate.JOYFUL_COLORS);

        recyclerView.setAdapter(adapter);
    }
}