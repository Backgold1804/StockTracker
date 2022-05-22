//package com.example.stocktracker.TableFragment;
//
//import android.annotation.SuppressLint;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.stocktracker.R;
//
//import java.util.ArrayList;
//
//public class TradingAdpater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//    private ArrayList<TradingData> tradingData = new ArrayList<>();
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewTyipe) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tradingitem, parent, false);
//
//        return new TradingViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
//        TradingViewHolder viewHolderItems = (TradingViewHolder) holder;
//        viewHolderItems.onBind(tradingData.get(position), position);
//    }
//
//    @Override
//    public int getItemCount() { return tradingData.size(); }
//    void addItem(TradingData tradingItem) { tradingData.add(tradingItem); }
//}
