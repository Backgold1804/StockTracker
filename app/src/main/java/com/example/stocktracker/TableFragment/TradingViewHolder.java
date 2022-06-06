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

    public TradingViewHolder(@NonNull View view) {
        super(view);
        this.view = view;

        date = view.findViewById(R.id._date);
        exchange = view.findViewById(R.id._exchange);
        unitPrice = view.findViewById(R.id._unit_price);
        orderAmount = view.findViewById(R.id._order_amount);
        imageButtonDelete = view.findViewById(R.id._delete_trading);

        tableRow = view.findViewById(R.id._table_row);
    }

    //  주어진 자리에 값을 Bind
    public void onBind(TradingItemData tradingItemData, int position) {
        //  가격을 보다 쉽게 확인하기 위한 Format
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");
        //  매매 uid, 날짜, 매매, 주문가격, 주문수량의 text를 설정
        int trading_uid = tradingItemData.getMy_stock_uid();
        date.setText(tradingItemData.getDate());
        exchange.setText("B".equals(tradingItemData.getExchange()) ? "매수" : "매도");
        unitPrice.setText(priceFormat.format(tradingItemData.getOrder_price()));
        orderAmount.setText(priceFormat.format(tradingItemData.getOrder_amount()));

        //  매매내역 삭제를 위한 Listener
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
                                deleteTrading(trading_uid, position);
                            }
                        })
                        .create()
                        .show();
            }
        });
    }

    //  매매내역을 지우기 위한 method
    private void deleteTrading(int trading_uid, int position) {
        //  서버와 통신하여 매매내역을 지움
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.deleteTrading(trading_uid);

        //  서버에서 매매내역을 지웠는지 확인
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                //  통신이 되었는지 확인
                Log.d("retrofit", "Delete Trading fetch Success");

                //  통신이 성공적으로 되었는지 확인
                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    //  data를 지웠는지 확인
                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("알림")
                            .setMessage(data.getResponse_msg())
                            .setPositiveButton("확인", null)
                            .create()
                            .show();

                    //  성공적으로 지웠으면 RecyclerView 갱신
                    if ("000".equals(data.getResponse_cd())) {
                        TradingAdpater adpater = (TradingAdpater) getBindingAdapter();
                        adpater.deleteItem(position);
                        adpater.notifyItemRemoved(position);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
