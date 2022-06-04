package com.example.stocktracker.FriendFragment;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    TextView textViewNickname;

    public FriendViewHolder(View view) {
        super(view);

        textViewNickname = view.findViewById(R.id.account_nickname);
    }

    public void onBind(FriendData data) {
        textViewNickname.setText(data.getNickname());
    }
}
