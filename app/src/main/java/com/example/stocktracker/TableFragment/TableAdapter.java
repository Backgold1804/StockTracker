package com.example.stocktracker.TableFragment;

import android.annotation.SuppressLint;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class TableAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    //  adapter에 들어갈 list
    private ArrayList<TableItemData> listData = new ArrayList<>();
    //  Item의 클릭 상태를 저장할 array 객체
    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    //  직전에 클릭했던 Item의 position
    private int prePosition = -1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //  xml파일로 View객체를 생성
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tableitem, parent, false);

        return new TableViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        TableViewHolder viewHolderItems = (TableViewHolder) holder;
        //  veiwHolderItems에 클릭한 아이템을 바인딩
        viewHolderItems.onBind(listData.get(position), position, selectedItems);

        viewHolderItems.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
                if (selectedItems.get(position)) {
                    //  펼처진 Item을 클릭하면 seletedItems에서 제거
                    selectedItems.delete(position);
                } else {
                    //  직전에 클릭했던 seletedItems를 제거
                    selectedItems.delete(prePosition);
                    //  클릭된 Items의 position을 저장
                    selectedItems.put(position, true);
                }

                //  클릭한 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                //  클릭된 position을 prePosition에 저장
                prePosition = position;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    //  외부에서 item을 추가
    void addItem(TableItemData tableItemData) {
        listData.add(tableItemData);
    }

    public String sumItem() {
        int sum = 0;
        DecimalFormat df = new DecimalFormat("###,###,###");

        for(int i = 0; i < listData.size(); i++) {
            sum += listData.get(i).current_price * listData.get(i).holding_quantity;
        }

        return df.format(sum);
    }

    public String getTotalProfit() {
        DecimalFormat df = new DecimalFormat("###,###,###");
        int profit = 0;
        int avg = 0;
        int cur = 0;
        for(int i = 0; i < listData.size(); i++) {
            avg += listData.get(i).average_unit_price * listData.get(i).holding_quantity;
            cur += listData.get(i).current_price * listData.get(i).holding_quantity;
        }

        profit = cur - avg;

        return df.format(profit);
    }

    public String getProfitRate() {
        DecimalFormat df = new DecimalFormat("##.##");
        float profitrate = 0;
        int avg = 0;
        int cur = 0;
        for(int i = 0; i < listData.size(); i++) {
            avg += listData.get(i).average_unit_price * listData.get(i).holding_quantity;
            cur += listData.get(i).current_price * listData.get(i).holding_quantity;
        }

        profitrate = (float) (cur - avg)/avg * 100;

        return df.format(profitrate);
    }
}
