package com.example.stocktracker.FriendFragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.Data;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateFriendViewHolder extends RecyclerView.ViewHolder {

    TextView textViewNickname;
    ImageButton imageButtonRemove;
    View view;
    int custUid;

    public UpdateFriendViewHolder(View view, int cust_uid) {
        super(view);

        this.custUid = cust_uid;
        this.view = view;
        textViewNickname = view.findViewById(R.id.account_nickname);
        imageButtonRemove = view.findViewById(R.id.remove_friend_button);
    }

    public void onBind(FriendData data, int position) {
        textViewNickname.setText(data.getNickname());
        imageButtonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = data.getNickname();
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("알림")
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteFriend(nickname, position);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void deleteFriend(String nickname, int position) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.deleteFriend(custUid, nickname);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Delete Friend fetch Success");

                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    Toast.makeText(view.getContext(), data.getResponse_msg(), Toast.LENGTH_LONG).show();

                    FriendData friendData = new FriendData();
                    friendData.setNickname(nickname);

                    UpdateFriendAdapter adapter = (UpdateFriendAdapter) getBindingAdapter();
                    adapter.deleteItem(position);
                    adapter.notifyItemRemoved(position);
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
