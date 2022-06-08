package com.example.stocktracker.FriendFragment;

import android.app.Dialog;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    TextView textViewNickname;
    View view;
    int custUid;
    Dialog chartDialog = null;

    public FriendViewHolder(View view) {
        super(view);

        this.view = view;
        textViewNickname = view.findViewById(R.id.account_nickname);
    }

    //  값을 bind
    public void onBind(FriendData data, int custUid) {
        textViewNickname.setText(data.getNickname());
        this.custUid = custUid;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(custUid, v);
                getData(data.getFriend_uid(), v);
            }
        });
    }

    private void drawDialog(View v) {
        if(chartDialog == null) {
            chartDialog = new Dialog(v.getContext());
            chartDialog.setContentView(R.layout.dialog_friend_chart);

            WindowManager.LayoutParams params = chartDialog.getWindow().getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;

            Button ok = (Button) chartDialog.findViewById(R.id.ok_btn);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    chartDialog.dismiss();
                }
            });
        } else {
            chartDialog.show();
        }
    }

    private void setChart(View v, int userUid, int size, BarEntry entry){
        HorizontalBarChart stackedChart;
        drawDialog(v);
        if (this.custUid == userUid)
            stackedChart = (HorizontalBarChart) chartDialog.findViewById(R.id.my_chart);
        else
            stackedChart = (HorizontalBarChart) chartDialog.findViewById(R.id.friend_chart);

        stackedChart.getAxisRight().setMinWidth(0.0f);
        stackedChart.getAxisRight().setMaxWidth(1.0f);

        XAxis xAxis = stackedChart.getXAxis();
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawLabels(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(size, true);
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(size));
        xAxis.setGranularity(1.0f);


//        stackedChart.setDescription(false);
        Legend l = stackedChart.getLegend();
        l.setEnabled(true);
        l.setWordWrapEnabled(true);
        l.setYEntrySpace(1.0f);
        LegendEntry l1 = new LegendEntry("QTotalAvgList", Legend.LegendForm.LINE, 10f, 2f, null, Color.parseColor("#0000FF"));
        LegendEntry l2 = new LegendEntry("QAmpTotalAvgList", Legend.LegendForm.LINE, 10f, 2f, null, Color.parseColor("#FF8000"));

        stackedChart.setVisibility(View.VISIBLE);
        stackedChart.animateXY(1000, 1000);
        stackedChart.getDescription().setEnabled(false);
        stackedChart.setExtraOffsets(-10, -10, -10, -10);
        stackedChart.setDrawGridBackground(false);
        stackedChart.setDrawBarShadow(false);
        stackedChart.setDrawValueAboveBar(true);


        ArrayList<BarEntry> dataList = new ArrayList<BarEntry>();
        dataList.add(entry);

        stackedChart.setTouchEnabled(false);

        BarDataSet barDataSet;
        if (custUid == userUid)
            barDataSet = new BarDataSet(dataList, "내 종목 비중");
        else
            barDataSet = new BarDataSet(dataList, "친구 종목 비중");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        BarData barData = new BarData(barDataSet);
        stackedChart.setData(barData);
        stackedChart.invalidate();
    }

    private void getData(int userUid, View v) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectFriendStockList(userUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                //   통신 했을 때
                Log.d("retrofit", "Select Friend Stock List fetch Success");

                //   통신에 성공 했을 때
                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();
                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    int sum = 0;

                    List<Map<String, Object>> stockList = data.getDatas();
                    for (Map<String, Object> map : stockList) {
                        Log.d("STOCK_LIST", map.get("stock_name").toString() + "(" + map.get("close_price").toString() + "원, " + map.get("holdings").toString() + "주)");
                    }

                    ArrayList<Integer> stockPrices = new ArrayList<>();
                    for (Map<String, Object> map : stockList) {
                        int stock = Integer.parseInt(map.get("close_price").toString()) * Integer.parseInt(map.get("holdings").toString());
                        stockPrices.add(stock);
                        sum += stock;
                    }

                    ArrayList<Float> weightList = new ArrayList<Float>();
                    if (sum > 0) {
                        for (Integer i : stockPrices) {
                            int price = i.intValue();
                            weightList.add((float) price / sum);
                        }
                    }

                    int mSize = weightList.size();

                    float[] mArray = toFloatArray(weightList);
                    BarEntry mEntry = new BarEntry(0, mArray);

                    setChart(v, userUid, mSize, mEntry);
                }
            }
            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public float[] toFloatArray(ArrayList<Float> arrayList) {
        if (arrayList == null)
            return null;

        if (arrayList.size() == 0)
            return new float[0];

        final int size = arrayList.size();
        float[] array = new float[size];
        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i).floatValue();
        }

        return array;
    }
}
