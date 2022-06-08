package com.example.stocktracker.TableFragment;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;

public class ChartViewHolder extends RecyclerView.ViewHolder {
    TextView stock_name, amount_price;
    LinearLayout legend;

    public ChartViewHolder(@NonNull View view) {
        super(view);
        stock_name = view.findViewById(R.id.legend_stock_name);
        amount_price = view.findViewById(R.id.legend_amount_price);
        legend = view.findViewById(R.id.legend);
    }

    public void onBind(ChartData chartData, int position, int colors) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        stock_name.setText(chartData.getChart_stock_name());
        amount_price.setText(df.format(chartData.getChart_holdings()*chartData.getChart_current_price())+"원");
        legend.setBackgroundColor(colors);
    }
}
