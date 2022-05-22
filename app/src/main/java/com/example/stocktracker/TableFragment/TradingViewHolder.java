//package com.example.stocktracker.TableFragment;
//
//import android.view.View;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.stocktracker.R;
//
//import java.text.DecimalFormat;
//
//public class TradingViewHolder extends RecyclerView.ViewHolder {
//    TextView date, exchange, unit_price, quantity;
//
//    public TradingViewHolder(@NonNull View itemView) {
//        super(itemView);
//        date = itemView.findViewById(R.id.date);
//        exchange = itemView.findViewById(R.id.exchange);
//        unit_price = itemView.findViewById(R.id.unit_price);
//        quantity = itemView.findViewById(R.id.quantity);
//    }
//
//    public void onBind(TradingData tradingData, int position) {
//        DecimalFormat df = new DecimalFormat("###,###");
//        date.setText("" + tradingData.getDate());
//        exchange.setText(tradingData.getExchange());
//        unit_price.setText(df.format(tradingData.getUnitPrice()));
//        quantity.setText(df.format(tradingData.getQuantity()));
//    }
//}
