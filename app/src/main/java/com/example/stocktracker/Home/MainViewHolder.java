package com.example.stocktracker.Home;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import  com.example.stocktracker.R;

import java.text.DecimalFormat;

public class MainViewHolder extends RecyclerView.ViewHolder {

    TextView homeStockName, homeAmountPrice, homeHoldings, homeProfit;

    public MainViewHolder(@NonNull View view) {
        super(view);

        homeStockName = view.findViewById(R.id.home_stock_name);
        homeAmountPrice = view.findViewById(R.id.home_amount_price);
        homeHoldings = view.findViewById(R.id.home_holdings);
        homeProfit = view.findViewById(R.id.home_profit);
    }

    //  값을 bind
    public void onBind(Data data) {
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");
        DecimalFormat rateFormat = new DecimalFormat("##.##");

        homeStockName.setText(data.getHome_stock_name());
        homeAmountPrice.setText(priceFormat.format(data.getHome_amount_price())+"원");
        homeHoldings.setText(priceFormat.format(data.getHome_holdings())+"주");
        homeProfit.setText(rateFormat.format(data.getHome_profit())+"%");
    }
}
