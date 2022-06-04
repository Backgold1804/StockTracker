package com.example.stocktracker.TableFragment;

import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.Data;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import java.text.DecimalFormat;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TradingViewHolder extends RecyclerView.ViewHolder {

    TextView date, exchange, unitPrice, orderAmount;
    ImageButton imageButtonDelete;
    View view;

    TableRow tableRow;
    TableAdapter tableAdapter;

    public TradingViewHolder(@NonNull View view, TableAdapter tableAdapter) {
        super(view);
        this.view = view;
        this.tableAdapter = tableAdapter; // 갱신을 위한 초기화

        date = view.findViewById(R.id._date);
        exchange = view.findViewById(R.id._exchange);
        unitPrice = view.findViewById(R.id._unit_price);
        orderAmount = view.findViewById(R.id._order_amount);
        imageButtonDelete = view.findViewById(R.id._delete_trading);

        tableRow = view.findViewById(R.id._table_row);
    }

    public void onBind(TradingItemData tradingItemData) {
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");
        int trading_uid = tradingItemData.getMy_stock_uid();
        date.setText(tradingItemData.getDate());
        exchange.setText("B".equals(tradingItemData.getExchange()) ? "매수" : "매도");
        unitPrice.setText(priceFormat.format(tradingItemData.getOrder_price()));
        orderAmount.setText(priceFormat.format(tradingItemData.getOrder_amount()));

        imageButtonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("알림")
                        .setMessage("삭제하시겠습니까?")
                        .setNegativeButton("취소", null)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTrading(trading_uid);
                                tableAdapter.getTableFragment().onResume(); // 갱신
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    private void deleteTrading(int trading_uid) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.deleteTrading(trading_uid);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Delete Trading fetch Success");

                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("알림")
                            .setMessage(data.getResponse_msg())
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
