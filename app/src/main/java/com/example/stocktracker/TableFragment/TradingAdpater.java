package com.example.stocktracker.TableFragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class TradingAdpater extends RecyclerView.Adapter<TradingViewHolder> {

    private List<TradingItemData> listData = new ArrayList<>();
    TableAdapter tableAdapter;

    TradingAdpater(List<TradingItemData> listData, TableAdapter tableAdapter) {
        this.listData = listData;
        this.tableAdapter = tableAdapter;
    }

    @Override
    public TradingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tradingitem, parent, false);

        return new TradingViewHolder(view, tableAdapter);
    }

    @Override
    public void onBindViewHolder(@NonNull TradingViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
}
