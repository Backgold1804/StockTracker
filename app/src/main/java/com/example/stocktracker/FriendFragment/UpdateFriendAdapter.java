package com.example.stocktracker.FriendFragment;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

import java.util.ArrayList;
import java.util.List;

public class UpdateFriendAdapter extends RecyclerView.Adapter<UpdateFriendViewHolder> {

    private List<FriendData> listData = new ArrayList<>();
    private int custUid;

    UpdateFriendAdapter(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public UpdateFriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.updatefrienditem, parent, false);
        return new UpdateFriendViewHolder(view, custUid);
    }

    @Override
    public void onBindViewHolder(UpdateFriendViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(FriendData data) {
        listData.add(data);
    }
}
