package com.example.stocktracker.TableFragment;

import android.animation.ValueAnimator;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;

public class TableViewHolder extends RecyclerView.ViewHolder {

    TextView country, stockName, ticker, currentPrice, blendedPrice, profitRate, holdings, amountPrice, profit;
    LinearLayout linearLayout;
    TableLayout visibleLayout;
    RecyclerView recyclerView;

    OnViewHolderItemClickListener onViewHolderItemClickListener;

    public TableViewHolder(@NonNull View view) {
        super(view);

        country = view.findViewById(R.id.country);
        stockName = view.findViewById(R.id.stock_name);
        ticker = view.findViewById(R.id.ticker);
        currentPrice = view.findViewById(R.id.current_price);
        blendedPrice = view.findViewById(R.id.blended_price);
        profit = view.findViewById(R.id.profit);
        profitRate = view.findViewById(R.id.profit_rate);
        holdings = view.findViewById(R.id.holdings);
        amountPrice = view.findViewById(R.id.amount_price);

        linearLayout = view.findViewById(R.id.linear_layout);
        visibleLayout = view.findViewById(R.id.visible_layout);
        recyclerView = view.findViewById(R.id.trading_recycler_view);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    public void onBind(TableItemData tableItemData, int position, SparseBooleanArray selectedItems) {
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");
        DecimalFormat rateFormat = new DecimalFormat("##.##");

        country.setText(tableItemData.getCountry());
        stockName.setText(tableItemData.getStock_name());
        ticker.setText(String.valueOf(tableItemData.getTicker()));
        currentPrice.setText(priceFormat.format(tableItemData.getCurrent_price()));
        blendedPrice.setText(priceFormat.format(tableItemData.getBlended_price()));
        profit.setText(priceFormat.format(tableItemData.getProfit() * tableItemData.getHoldings()));
        profitRate.setText(rateFormat.format(tableItemData.getProfit_rate()) + "%");
        holdings.setText(String.valueOf(tableItemData.getHoldings()));
        amountPrice.setText(priceFormat.format(tableItemData.getCurrent_price() * tableItemData.getHoldings()));

        changeVisibility(selectedItems.get(position));
    }

    private void changeVisibility(final boolean isExpanded) {
        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);

        va.setDuration(500);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                visibleLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        va.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}
