package com.example.stocktracker.TableFragment;

import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;

public class TradingViewHolder extends RecyclerView.ViewHolder {
    TextView date, exchange, unitPrice, orderAmount;
    TableRow tableRow;

    public TradingViewHolder(@NonNull View view) {
        super(view);

        date = view.findViewById(R.id._date);
        exchange = view.findViewById(R.id._exchange);
        unitPrice = view.findViewById(R.id._unit_price);
        orderAmount = view.findViewById(R.id._order_amount);

        tableRow = view.findViewById(R.id._table_row);
    }

    public void onBind(TradingItemData tradingItemData) {
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");
        date.setText(tradingItemData.getDate());
        exchange.setText("B".equals(tradingItemData.getExchange()) ? "매수" : "매도");
        unitPrice.setText(priceFormat.format(tradingItemData.getOrder_price()));
        orderAmount.setText(priceFormat.format(tradingItemData.getOrder_amount()));
    }
}
