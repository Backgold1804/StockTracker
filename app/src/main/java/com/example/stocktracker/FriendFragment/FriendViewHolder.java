package com.example.stocktracker.FriendFragment;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.R;

public class FriendViewHolder extends RecyclerView.ViewHolder {

    TextView textViewNickname;

    public FriendViewHolder(View view) {
        super(view);

        textViewNickname = view.findViewById(R.id.account_nickname);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAbsoluteAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {

                }
            }
        });
    }

    //  값을 bind
    public void onBind(FriendData data) {
        textViewNickname.setText(data.getNickname());
    }
}
