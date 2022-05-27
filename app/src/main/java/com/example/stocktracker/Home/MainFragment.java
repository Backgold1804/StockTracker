package com.example.stocktracker.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {

    MainAdapter adapter;

    private int custUid;
    private View view;

    public MainFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new MainAdapter(custUid);

        init();
        getData();

        return view;
    }

    private void init() {
        RecyclerView recyclerView = view.findViewById(R.id.home_status);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.divider));
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call =networkService.selectStockList(custUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    if ("000".equals(data.getResponse_cd())) {
                        for (Map map : data.getDatas()) {
                            int homeAmountPrice = Integer.parseInt(map.get("close_price").toString()) * Integer.parseInt(map.get("holdings").toString());
                            int homeUserPrice = Integer.parseInt(map.get("blended_price").toString()) * Integer.parseInt(map.get("holdings").toString());
                            Data itemData = new Data();
                            itemData.setHome_stock_name(map.get("stock_name").toString());
                            itemData.setHome_amount_price(homeAmountPrice);
                            itemData.setHome_holdings(Integer.parseInt(map.get("holdings").toString()));
                            itemData.setHome_profit(Float.parseFloat(map.get("profit_rate").toString()));
                            itemData.setHome_user_price(homeUserPrice);
                            adapter.addItem(itemData);
                        }

                        TextView totalPrice = (TextView) view.findViewById(R.id.home_total_price);
                        TextView totalProfit = (TextView) view.findViewById(R.id.home_total_profit);

                        totalPrice.setText(adapter.sumPrice() + "원");
                        totalProfit.setText(adapter.sumProfitRate() + "% (" + adapter.sumProfit() +"원)");
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
}