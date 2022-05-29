package com.example.stocktracker.TableFragment;

import android.animation.ValueAnimator;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.Data;
import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableViewHolder extends RecyclerView.ViewHolder {

    TextView country, stockName, ticker, currentPrice, blendedPrice, profitRate, holdings, amountPrice, profit, sell_stock_name, buy_stock_name;
    LinearLayout linearLayout;
    TableLayout visibleLayout;
    RecyclerView recyclerView;

    Button sellButton, buyButton;

    final Calendar myCalendar = Calendar.getInstance();
    private int custUid;

    private Context context;
    TableAdapter adapter = new TableAdapter(custUid);

    OnViewHolderItemClickListener onViewHolderItemClickListener;

    public TableViewHolder(@NonNull View view, int cust_uid) {
        super(view);

        context = view.getContext();

        this.custUid = cust_uid;

        country = view.findViewById(R.id.country);
        stockName = view.findViewById(R.id.stock_name);
        ticker = view.findViewById(R.id.ticker);
        currentPrice = view.findViewById(R.id.current_price);
        blendedPrice = view.findViewById(R.id.blended_price);
        profit = view.findViewById(R.id.profit);
        profitRate = view.findViewById(R.id.profit_rate);
        holdings = view.findViewById(R.id.holdings);
        amountPrice = view.findViewById(R.id.amount_price);


        linearLayout = view.findViewById(R.id.linear_layout);
        visibleLayout = view.findViewById(R.id.visible_layout);
        recyclerView = view.findViewById(R.id.trading_recycler_view);

        sellButton = view.findViewById(R.id.sell_button);
        buyButton = view.findViewById(R.id.buy_button);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewHolderItemClickListener.onViewHolderItemClick();
            }
        });
    }

    public void onBind(TableItemData tableItemData, int position, SparseBooleanArray selectedItems) {
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");
        DecimalFormat rateFormat = new DecimalFormat("##.##");

        country.setText(tableItemData.getCountry());
        stockName.setText(tableItemData.getStock_name());
        ticker.setText(String.valueOf(tableItemData.getTicker()));
        currentPrice.setText(priceFormat.format(tableItemData.getCurrent_price()));
        blendedPrice.setText(priceFormat.format(tableItemData.getBlended_price()));
        profit.setText(priceFormat.format(tableItemData.getProfit() * tableItemData.getHoldings()));
        profitRate.setText(rateFormat.format(tableItemData.getProfit_rate()) + "%");
        holdings.setText(String.valueOf(tableItemData.getHoldings()));
        amountPrice.setText(priceFormat.format(tableItemData.getCurrent_price() * tableItemData.getHoldings()));

        /* 매매 Dialog */
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog buyDialog = new Dialog(view.getContext());
                buyDialog.setContentView(R.layout.buy_dialog);
                buyDialog.setTitle("매수");
                buy_stock_name = buyDialog.findViewById(R.id.buy_stock_name);
                buy_stock_name.setText(tableItemData.getStock_name());

                WindowManager.LayoutParams params = buyDialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;

                Button buy = (Button) buyDialog.findViewById(R.id.buy_btn);
                Button cancel = (Button) buyDialog.findViewById(R.id.cancel_buy_button);

                final TextView buy_stock_name = (TextView) buyDialog.findViewById(R.id.buy_stock_name);
                final EditText buy_price = (EditText) buyDialog.findViewById(R.id.buy_price_edit);
                final EditText buy_amount = (EditText) buyDialog.findViewById(R.id.buy_amount_edit);
                final EditText buy_date = (EditText) buyDialog.findViewById(R.id.buy_date_edit);
                final EditText buy_time = (EditText) buyDialog.findViewById(R.id.buy_time_edit);

                buy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stock_name = buy_stock_name.getText().toString();
                        String string_price = buy_price.getText().toString();
                        String string_amount = buy_amount.getText().toString();
                        String date = buy_date.getText().toString();
                        String time = buy_time.getText().toString();
                        // 매수 버튼 구현

                        if (string_price.trim().length() == 0 || string_amount.trim().length() == 0 || date.trim().length() == 0 || time.trim().length() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("알림")
                                    .setMessage("정보를 입력해주세요.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                        } else {
                            int price = Integer.parseInt(string_price);
                            int amount = Integer.parseInt(string_amount);
                            putTrading(stock_name, "B", price, amount, date, time);
                            buyDialog.dismiss();
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buyDialog.dismiss();
                    }
                });

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String dateFormat = "yyyy-MM-dd";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.KOREA);
                        buy_date.setText(simpleDateFormat.format(myCalendar.getTime()));

                    }
                };

                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);

                        String timeFormat = "HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.KOREA);
                        buy_time.setText(simpleDateFormat.format(myCalendar.getTime()));
                    }
                };

                buy_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            new DatePickerDialog(view.getContext(), date,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    }
                });

                buy_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(view.getContext(), date,
                                myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                buy_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                            int minute = myCalendar.get(Calendar.MINUTE);

                            TimePickerDialog timePicker;
                            timePicker = new TimePickerDialog(view.getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, time, hour, minute, false);
                            timePicker.setTitle("매수시간");
                            timePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            timePicker.show();
                        }

                    }
                });

                buy_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                        int minute = myCalendar.get(Calendar.MINUTE);

                        TimePickerDialog timePicker;
                        timePicker = new TimePickerDialog(view.getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, time, hour, minute, false);
                        timePicker.setTitle("매수시간");
                        timePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        timePicker.show();
                    }
                });

                buyDialog.show();
            }
        });

        sellButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog sellDialog = new Dialog(view.getContext());
                sellDialog.setContentView(R.layout.sell_dialog);
                sellDialog.setTitle("매도");
                sell_stock_name = sellDialog.findViewById(R.id.sell_stock_name);
                sell_stock_name.setText(tableItemData.getStock_name());

                WindowManager.LayoutParams params = sellDialog.getWindow().getAttributes();
                params.width = WindowManager.LayoutParams.MATCH_PARENT;

                Button sell = (Button) sellDialog.findViewById(R.id.sell_btn);
                Button cancel = (Button) sellDialog.findViewById(R.id.cancel_sell_button);

                final TextView sell_stock_name = sellDialog.findViewById(R.id.sell_stock_name);
                final EditText sell_price= (EditText) sellDialog.findViewById(R.id.sell_price_edit);
                final EditText sell_amount = (EditText) sellDialog.findViewById(R.id.sell_amount_edit);
                final EditText sell_date = (EditText) sellDialog.findViewById(R.id.sell_date_edit);
                final EditText sell_time = (EditText) sellDialog.findViewById(R.id.sell_time_edit);

                sell.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String stock_name = sell_stock_name.getText().toString();
                        String string_price = sell_price.getText().toString();
                        String string_amount = sell_amount.getText().toString();
                        String date = sell_date.getText().toString();
                        String time = sell_time.getText().toString();
                        // 매도 버튼 구현
                        if (string_price.trim().length() == 0 || string_amount.trim().length() == 0 || date.trim().length() == 0 || time.trim().length() == 0) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                            builder.setTitle("알림")
                                    .setMessage("정보를 입력해주세요.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                        } else {
                            int price = Integer.parseInt(string_price);
                            int amount = Integer.parseInt(string_amount);
                            if (amount > Integer.parseInt(holdings.getText().toString())) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                                builder.setTitle("알림")
                                        .setMessage("매도수량이 너무 많습니다.")
                                        .setPositiveButton("확인", null)
                                        .create()
                                        .show();
                            } else {
                                putTrading(stock_name, "S", price, amount, date, time);
                                sellDialog.dismiss();
                            }
                        }
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sellDialog.dismiss();
                    }
                });

                DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalendar.set(Calendar.YEAR, year);
                        myCalendar.set(Calendar.MONTH, month);
                        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                        String dateFormat = "yyyy-MM-dd";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.KOREA);
                        sell_date.setText(simpleDateFormat.format(myCalendar.getTime()));

                    }
                };

                TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        myCalendar.set(Calendar.MINUTE, minute);

                        String timeFormat = "HH:mm:ss";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(timeFormat, Locale.KOREA);
                        sell_time.setText(simpleDateFormat.format(myCalendar.getTime()));
                    }
                };

                sell_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            new DatePickerDialog(view.getContext(), date,
                                    myCalendar.get(Calendar.YEAR),
                                    myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    }
                });

                sell_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new DatePickerDialog(view.getContext(), date,
                                myCalendar.get(Calendar.YEAR),
                                myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                    }
                });

                sell_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                            int minute = myCalendar.get(Calendar.MINUTE);

                            TimePickerDialog timePicker;
                            timePicker = new TimePickerDialog(view.getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, time, hour, minute, false);
                            timePicker.setTitle("매도시간");
                            timePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            timePicker.show();
                        }
                    }
                });

                sell_time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                        int minute = myCalendar.get(Calendar.MINUTE);

                        TimePickerDialog timePicker;
                        timePicker = new TimePickerDialog(view.getContext(),android.R.style.Theme_Holo_Light_Dialog_NoActionBar, time, hour, minute, false);
                        timePicker.setTitle("매도시간");
                        timePicker.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        timePicker.show();
                    }
                });

                sellDialog.show();
            }


        });
        /* 매매 Dialog */
        changeVisibility(selectedItems.get(position));
    }

    public void putTrading(String stock_name, String trading, int price, int amount, String date, String time) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.putTrading(custUid, stock_name, trading, price, amount, date, time);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg() + "(putTrading)");

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
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

    private void changeVisibility(final boolean isExpanded) {
        ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 600) : ValueAnimator.ofInt(600, 0);

        va.setDuration(500);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                visibleLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
            }
        });
        va.start();
    }

    public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
        this.onViewHolderItemClickListener = onViewHolderItemClickListener;
    }
}
