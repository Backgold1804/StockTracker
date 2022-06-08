package com.example.stocktracker.Home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private List<Data> listData = new ArrayList<>();

    private int userUid;

    MainAdapter(int user_uid) {
     this.userUid = user_uid;
    }

    @NonNull
    @Override
    public  MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    //  값을 추가
    public void addItem(Data data) {
        listData.add(data);
    }

    //  총평가금액을 계산하는 method
    public String sumPrice() {
        int sum = 0;
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");

        for (int i = 0; i < listData.size(); i++) {
            sum += listData.get(i).home_amount_price;
        }

        return priceFormat.format(sum);
    }

    //  수익을 계산하는 method
    public String sumProfitRate() {
        float profitRate = 0;
        int avg = 0;
        int cur = 0;
        DecimalFormat rateFormat = new DecimalFormat("##.##");

        for (int i = 0; i < listData.size(); i++) {
            avg += listData.get(i).home_user_price * listData.get(i).home_holdings;
            cur += listData.get(i).home_amount_price * listData.get(i).home_holdings;
        }

        profitRate = (float) (cur - avg) / avg * 100;

        if (Float.isNaN(profitRate)) profitRate = 0;

        return rateFormat.format(profitRate);
    }

    //  수익률을 계산하는 method
    public String sumProfit() {
        int profit = 0;
        int avg = 0;
        int cur = 0;
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");

        for (int i = 0; i < listData.size(); i++) {
            avg += listData.get(i).home_user_price;
            cur += listData.get(i).home_amount_price;
        }

        profit = cur - avg;

        return priceFormat.format(profit);
    }
}
