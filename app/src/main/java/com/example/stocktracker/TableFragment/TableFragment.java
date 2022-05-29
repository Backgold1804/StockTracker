package com.example.stocktracker.TableFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.stocktracker.Data;
import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableFragment extends Fragment {

    TableAdapter adapter;

    private View view;
    private int custUid;

    final Calendar myCalendar = Calendar.getInstance();
    List<String> stockList = new ArrayList<>();

    public TableFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_table, container, false);
        adapter = new TableAdapter(custUid);

        setHasOptionsMenu(true);

        init();
        getData();
        setStockList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.app_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_buy:
                showDialog();
                return true;

            default:

                return true;
        }
    }

    private void init() {
        RecyclerView recyclerView = view.findViewById(R.id.table_recycler_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
    }

    public void getData() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectStockList(custUid);
        Log.d("TAG", "" + custUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                Log.d("retrofit", "Find Stock List fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg() + "(selectStockList)");

                    if ("000".equals(data.getResponse_cd())) {
                        adapter.clear();
                        for (Map map : data.getDatas()) {
                            TableItemData itemData = new TableItemData();
                            itemData.setMy_stock_uid(Integer.parseInt(map.get("my_stock_uid").toString()));
                            itemData.setCountry(map.get("country").toString());
                            itemData.setStock_name(map.get("stock_name").toString());
                            itemData.setTicker(map.get("ticker").toString());
                            itemData.setCurrent_price(Integer.parseInt(map.get("close_price").toString()));
                            itemData.setBlended_price(Integer.parseInt(map.get("blended_price").toString()));
                            itemData.setHoldings(Integer.parseInt(map.get("holdings").toString()));
                            itemData.setProfit(Integer.parseInt(map.get("profit").toString()));
                            itemData.setProfit_rate(Float.parseFloat(map.get("profit_rate").toString()));
                            Log.d("TAG", map.get("stock_name").toString());
                            adapter.addItem(itemData);
                        }

                        TextView totalamount = (TextView) view.findViewById(R.id.total_amount);
                        TextView totalprofit = (TextView) view.findViewById(R.id.total_profit);
                        TextView totalprofitrate = (TextView) view.findViewById(R.id.total_profit_rate);
                        totalamount.setText("총평가금액 "+adapter.sumItem()+"원");
                        totalprofit.setText("미실현손익 "+adapter.getTotalProfit()+"원");
                        totalprofitrate.setText("총수익률 "+adapter.getProfitRate()+"%");

                        init();
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showDialog() {
        final Dialog createDialog = new Dialog(view.getContext());
        createDialog.setContentView(R.layout.create_dialog);
        createDialog.setTitle("신규");

        WindowManager.LayoutParams params = createDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;

        Button buy = (Button) createDialog.findViewById(R.id.buy_btn);
        Button cancel = (Button) createDialog.findViewById(R.id.cancel_buy_button);

        final AutoCompleteTextView buy_stock_name = (AutoCompleteTextView) createDialog.findViewById(R.id.buy_stock_name);
        final EditText buy_price = (EditText) createDialog.findViewById(R.id.buy_price_edit);
        final EditText buy_amount = (EditText) createDialog.findViewById(R.id.buy_amount_edit);
        final EditText buy_date = (EditText) createDialog.findViewById(R.id.buy_date_edit);
        final EditText buy_time = (EditText) createDialog.findViewById(R.id.buy_time_edit);

        buy_stock_name.setAdapter(new ArrayAdapter<String>(getContext(), android.R.layout.simple_dropdown_item_1line, stockList));

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String stock_name = buy_stock_name.getText().toString();
                String string_price = buy_price.getText().toString();
                String string_amount = buy_amount.getText().toString();
                String date = buy_date.getText().toString();
                String time = buy_time.getText().toString();

                // 매수 버튼 구현
                if (stock_name.trim().length() == 0 || string_price.trim().length() == 0 || string_amount.trim().length() == 0 || date.trim().length() == 0 || time.trim().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("알림")
                            .setMessage("정보를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    int price = Integer.parseInt(string_price);
                    int amount = Integer.parseInt(string_amount);
                    putStock(stock_name, price, amount, date, time);
                    createDialog.dismiss();
                    adapter.clear();
                    getData();
                    adapter.notifyDataSetChanged();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDialog.dismiss();
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

        createDialog.show();
    }

    public void setStockList() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.getStockList();

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg() + "(getStockList)");

                    if ("000".equals(data.getResponse_cd())) {
                        for (Map<String, Object> map :data.getDatas()) {
                            stockList.add(map.get("stock_name").toString());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void putStock(String stock_name, int price, int amount, String date, String time) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.putNewStock(custUid, stock_name, price, amount, date, time);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
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