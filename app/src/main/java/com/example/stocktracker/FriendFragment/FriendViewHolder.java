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
import com.github.mikephil.charting.components.YAxis;
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

    //  Dialog를 그리는 method
    private void drawDialog(View v) {
        if(chartDialog == null) {
            chartDialog = new Dialog(v.getContext());
            chartDialog.setContentView(R.layout.dialog_friend_chart);

            //  Dialog의 마진 설정
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

    //  각 사람의 chart를 설정하는 method
    private void setChart(View v, int userUid, int size, BarEntry entry, String[] name){
        HorizontalBarChart stackedChart;
        drawDialog(v);
        if (this.custUid == userUid)
            stackedChart = (HorizontalBarChart) chartDialog.findViewById(R.id.my_chart);
        else
            stackedChart = (HorizontalBarChart) chartDialog.findViewById(R.id.friend_chart);

        //  비중을 나타내기 위하여 최소값을 0, 최대값을 1로 설정
        stackedChart.getAxisRight().setMinWidth(0.0f);
        stackedChart.getAxisRight().setMaxWidth(1.0f);

        //  X축 숫자 제거
        XAxis xAxis = stackedChart.getXAxis();
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);

        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelCount(size, true);
        //xAxis.setValueFormatter(new MyXAxisValueFormatter(size));
        xAxis.setGranularity(1.0f);

        //  Y축 오른쪽 숫자 제거
        YAxis yAxisRight = stackedChart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawAxisLine(false);
        yAxisRight.setDrawZeroLine(false);

        //  Y축 왼쪽 숫자 제거
        YAxis yAxisLeft = stackedChart.getAxisLeft();
        yAxisLeft.setDrawLabels(false);
        yAxisLeft.setDrawAxisLine(false);
        yAxisLeft.setDrawZeroLine(false);

//        stackedChart.setDescription(false);
        Legend l = stackedChart.getLegend();
        l.setEnabled(true);
        l.setWordWrapEnabled(true);
        l.setYEntrySpace(1.0f);
        l.setXEntrySpace(10);
        LegendEntry l1 = new LegendEntry("QTotalAvgList", Legend.LegendForm.LINE, 10f, 2f, null, Color.parseColor("#0000FF"));
        LegendEntry l2 = new LegendEntry("QAmpTotalAvgList", Legend.LegendForm.LINE, 10f, 2f, null, Color.parseColor("#FF8000"));

        stackedChart.setVisibility(View.VISIBLE);
        stackedChart.animateXY(1000, 1000);
        //  Lavel 제거
        stackedChart.getDescription().setEnabled(false);
        stackedChart.setExtraOffsets(-10, -10, -10, -10);
        stackedChart.setDrawGridBackground(false);

        //  줌 설정 막기
        stackedChart.setPinchZoom(false);
        //  터치 설정 막기
        stackedChart.setTouchEnabled(false);
        //  그림자 설정 제거
        stackedChart.setDrawBarShadow(false);

        //  비중을 stack 위에 적음
        stackedChart.setDrawValueAboveBar(true);

        //  매수한 종목이 없을 때 나타낼 text 설정
        stackedChart.setNoDataText("보유중인 종목이 없습니다.");
        stackedChart.setNoDataTextColor(Color.BLACK);

        ArrayList<BarEntry> dataList = new ArrayList<BarEntry>();
        if (size > 0) {
            dataList.add(entry);

            //  친구 종목인지 자신의 종목인지 확인하여 chart data를 넣음
            BarDataSet barDataSet;
            if (custUid == userUid)
                barDataSet = new BarDataSet(dataList, "내 종목 비중");
            else
                barDataSet = new BarDataSet(dataList, "친구 종목 비중");
            barDataSet.setStackLabels(name);
            barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);

            BarData barData = new BarData(barDataSet);
            stackedChart.setData(barData);
        }
        stackedChart.invalidate();
    }

    //  data를 불러오는 method
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

                    //  개별 종목당 평가 금액을 ArrayList
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

                    ArrayList<String> nameList = new ArrayList<String>();
                    if (sum > 0) {
                        for (Map<String, Object> map : stockList) {
                            Log.d("STOCK_LIST", map.get("stock_name").toString() + "(" + map.get("close_price").toString() + "원, " + map.get("holdings").toString() + "주)");
                            nameList.add(map.get("stock_name").toString());
                        }
                    }

                    int mSize = weightList.size();

                    float[] mWeight = toFloatArray(weightList);
                    String[] mName = toStringArray(nameList);
                    BarEntry mEntry = new BarEntry(0, mWeight);

                    setChart(v, userUid, mSize, mEntry, mName);
                }
            }
            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private float[] toFloatArray(ArrayList<Float> arrayList) {
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

    private String[] toStringArray(ArrayList<String> arrayList) {
        if (arrayList == null)
            return null;

        if (arrayList.size() == 0)
            return new String[0];

        final int size = arrayList.size();
        String[] array = new String[size];
        for (int i = 0; i < size; i++) {
            array[i] = arrayList.get(i);
        }

        return array;
    }
}
