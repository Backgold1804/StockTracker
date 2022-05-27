package com.example.stocktracker.TableFragment;

import android.animation.ValueAnimator;
import android.app.Dialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;

public class TableViewHolder extends RecyclerView.ViewHolder {

    TextView country, stockName, ticker, currentPrice, blendedPrice, profitRate, holdings, amountPrice, profit, sell_stock_name, buy_stock_name;
    LinearLayout linearLayout;
    TableLayout visibleLayout;
    RecyclerView recyclerView;

    Button sellButton, buyButton;

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

        sellButton = view.findViewById(R.id.sell_button);
        buyButton = view.findViewById(R.id.buy_button);

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

        /* 매매 Dialog */
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog buyDialog = new Dialog(view.getContext());
                buyDialog.setContentView(R.layout.buy_dialog);
                buyDialog.setTitle("매수");
                buy_stock_name = buyDialog.findViewById(R.id.buy_stock_name);
                buy_stock_name.setText(tableItemData.getStock_name());

                Button buy = (Button) buyDialog.findViewById(R.id.buy_btn);
                Button cancel = (Button) buyDialog.findViewById(R.id.cancel_buy_button);

                final EditText buy_price = (EditText) buyDialog.findViewById(R.id.buy_price_edit);
                final EditText buy_amount = (EditText) buyDialog.findViewById(R.id.buy_amount_edit);
                final EditText buy_date = (EditText) buyDialog.findViewById(R.id.buy_date_edit);

                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 매수 버튼 구현

                        buyDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyDialog.dismiss();
                    }
                });

                buyDialog.show();
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog sellDialog = new Dialog(view.getContext());
                sellDialog.setContentView(R.layout.sell_dialog);
                sellDialog.setTitle("매도");
                sell_stock_name = sellDialog.findViewById(R.id.sell_stock_name);
                sell_stock_name.setText(tableItemData.getStock_name());

                Button sell = (Button) sellDialog.findViewById(R.id.sell_btn);
                Button cancel = (Button) sellDialog.findViewById(R.id.cancel_sell_button);

                final EditText sell_price= (EditText) sellDialog.findViewById(R.id.sell_price_edit);
                final EditText sell_amount = (EditText) sellDialog.findViewById(R.id.sell_amount_edit);
                final EditText sell_date = (EditText) sellDialog.findViewById(R.id.sell_date_edit);

                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 매도 버튼 구현

                        sellDialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sellDialog.dismiss();
                    }
                });

                sellDialog.show();
            }
        });
        /* 매매 Dialog */

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
