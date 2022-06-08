package com.example.stocktracker.TableFragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.util.ArrayList;
import java.util.List;

public class ChartAdapter extends RecyclerView.Adapter<ChartViewHolder> {
    List<ChartData> chartData = new ArrayList<ChartData>();
    int[] colors;

    ChartAdapter(List<ChartData> chartData, int[] colors) {
        this.chartData = chartData;
        this.colors = colors;
    }

    @NonNull
    @Override
    public ChartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chartitem, parent, false);
        return new ChartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChartViewHolder holder, @SuppressLint("Recyclerview") int position) {
        holder.onBind(chartData.get(position), position, colors[position]);
    }

    @Override
    public int getItemCount() { return chartData.size(); }
}
