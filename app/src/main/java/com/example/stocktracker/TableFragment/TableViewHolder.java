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
    TextView country, title, ticker, current_price, yesterday_price, average_unit_price, holding_quantity, profit,
            profit_rate, holding_weight, date, exchange, unit_price;
    LinearLayout linearLayout;
    TableLayout visible_layout;

    OnViewHolderItemClickListener onViewHolderItemClickListener;

    //  생성자
    public TableViewHolder(@NonNull View itemView) {
        super(itemView);
        country = itemView.findViewById(R.id.country);
        title = itemView.findViewById(R.id.title);
        ticker = itemView.findViewById(R.id.ticker);
        current_price = itemView.findViewById(R.id.current_price);
        yesterday_price = itemView.findViewById(R.id.yesterday_price);
        average_unit_price = itemView.findViewById(R.id.average_unit_price);
        holding_quantity = itemView.findViewById(R.id.holding_quantity);
        profit = itemView.findViewById(R.id.profit);
        profit_rate = itemView.findViewById(R.id.profit_rate);
        date = itemView.findViewById(R.id.date);
        exchange = itemView.findViewById(R.id.exchange);
        unit_price = itemView.findViewById(R.id.unit_price);
        linearLayout = itemView.findViewById(R.id.linearlayout);
        visible_layout = itemView.findViewById(R.id.visible_layout);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    //  클릭한 Item에 값을 바인딩
    public void onBind(TableItemData tableItemData, int position, SparseBooleanArray selectedItems) {
        DecimalFormat df = new DecimalFormat("###,###,###");
        country.setText(tableItemData.getCountry());
        title.setText(tableItemData.getTitle());
        ticker.setText(tableItemData.getTicker());
        current_price.setText(df.format(tableItemData.getCurrentPrice()));
        yesterday_price.setText(df.format(tableItemData.getCurrentPrice()*tableItemData.getHoldingQuantity()));
        average_unit_price.setText(df.format(tableItemData.getAverageUnitPrice()));
        holding_quantity.setText(df.format(tableItemData.getHoldingQuantity()));
        profit.setText(df.format(tableItemData.getProfit()*tableItemData.getHoldingQuantity()));
        profit_rate.setText(Float.toString(tableItemData.getProfitRate())+"%");
        date.setText(""+ tableItemData.getDate());
        exchange.setText(tableItemData.getExchange());
        unit_price.setText(df.format(tableItemData.getUnitPrice()));

        changeVisibility(selectedItems.get(position));
    }

    private void changeVisibility(final boolean isExpanded) {
        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);
        //  Animator가 실행되는 시간(ms)
        va.setDuration(500);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //  TableLayout이 나타나거나 사라지게 만드는 부분
                visible_layout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        //  Animation 시작
        va.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}
