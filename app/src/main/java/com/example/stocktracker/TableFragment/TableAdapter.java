package com.example.stocktracker.TableFragment;

import android.annotation.SuppressLint;
import android.text.Layout;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.ListData;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TableAdapter extends RecyclerView.Adapter<TableViewHolder> {

    private List<TableItemData> listData = new ArrayList<>();

    private SparseBooleanArray selectedItems = new SparseBooleanArray();

    private int prePosition = -1;

    private int custUid;
    TradingAdpater adapter;
    TableFragment tableFragment;

    TableAdapter(int cust_uid) {
        this.custUid = cust_uid;
    }

    @NonNull
    @Override
    public TableViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tableitem, parent, false);
        return new TableViewHolder(view, custUid, this);
    }

    @Override
    public void onBindViewHolder(@NonNull TableViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.onBind(listData.get(position), position, selectedItems);

        TableItemData itemData = listData.get(position);

        if (itemData.getData_list() == null) itemData.setData_list(addDataList(itemData.getMy_stock_uid()));

        //  RecyclerView를 접었다 폈다 하기 위한 Listener
        holder.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
                if (selectedItems.get(position)) {
                    selectedItems.delete(position);
                } else {
                    selectedItems.delete(prePosition);
                    selectedItems.put(position, true);
                }

                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);

                prePosition = position;
            }
        });

        init(holder, itemData);
    }

    private void init(TableViewHolder holder, TableItemData itemData) {
        RecyclerView recyclerView = holder.recyclerView.findViewById(R.id.trading_recycler_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.recyclerView.getContext());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new TradingAdpater(itemData.getData_list());

        //  구분선 추가
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(holder.recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setAdapter(adapter);
    }

    //  매매내역을 불러오는 method
    private List<TradingItemData> addDataList(int my_stock_uid) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectTradingList(my_stock_uid);

        List<TradingItemData> tradingItemData = new ArrayList<>();

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                //  통신을 했을 때
                Log.d("retrofit", "Find Trading List fetch success");

                //  통신에 성공했을 때
                if (response.isSuccessful() && response != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    //  매매내역을 추가
                    if ("000".equals(data.getResponse_cd())) {
                        for (Map map : data.getDatas()) {
                            TradingItemData itemData = new TradingItemData();
                            itemData.setMy_stock_uid(Integer.parseInt(map.get("uid").toString()));
                            itemData.setDate(map.get("insert_date").toString().split(" ")[0]);
                            itemData.setStock_name(map.get("stock_name").toString());
                            itemData.setExchange((map.get("trading").toString()));
                            itemData.setOrder_price(Integer.parseInt(map.get("order_price").toString()));
                            itemData.setOrder_amount(Integer.parseInt(map.get("order_amount").toString()));
                            tradingItemData.add(itemData);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ListData> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return tradingItemData;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(TableItemData tableItemData) {
        listData.add(tableItemData);
    }

    void setItem(String trading, TradingItemData tradingItemData, int position) {
        int temp_price = listData.get(position).getBlended_price() * listData.get(position).getHoldings();
        int holdings = "B".equals(trading) ? listData.get(position).getHoldings() + tradingItemData.getOrder_amount() : listData.get(position).getHoldings() - tradingItemData.getOrder_amount();
        int amount_price = holdings * listData.get(position).getCurrent_price();
        int order_amount = tradingItemData.getOrder_amount() * tradingItemData.getOrder_price();
        int blended_price = "B".equals(trading) ? (temp_price + order_amount) / holdings : listData.get(position).getBlended_price();
        int profit = listData.get(position).getCurrent_price() - blended_price;
        float profit_rate = (float) profit / blended_price * 100;

        listData.get(position).setHoldings(holdings);
        listData.get(position).setAmount_price(amount_price);
        listData.get(position).setBlended_price(blended_price);
        listData.get(position).setProfit(profit);
        listData.get(position).setProfit_rate(profit_rate);
    }

    public void clear() {
        listData.clear();
        prePosition = -1;
    }

    //  총평가금액을 계산하는 method
    public String sumItem() {
        int sum = 0;
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");

        for(int i = 0; i < listData.size(); i++) {
            sum += listData.get(i).current_price * listData.get(i).holdings;
        }

        return priceFormat.format(sum);
    }

    //  수익을 계산하는 method
    public String getTotalProfit() {
        int profit = 0;
        int avg = 0;
        int cur = 0;
        DecimalFormat priceFormat = new DecimalFormat("###,###,###");

        for (int i = 0; i < listData.size(); i++) {
            avg += listData.get(i).blended_price * listData.get(i).holdings;
            cur += listData.get(i).current_price * listData.get(i).holdings;
        }

        profit = cur - avg;

        return priceFormat.format(profit);
    }

    //  수익률을 계산하는 method
    public String getProfitRate() {
        float profitRate = 0;
        int avg = 0;
        int cur = 0;
        DecimalFormat rateFormat = new DecimalFormat("##.##");

        for (int i = 0; i < listData.size(); i++) {
            avg += listData.get(i).blended_price * listData.get(i).holdings;
            cur += listData.get(i).current_price * listData.get(i).holdings;
        }

        profitRate = (float) (cur - avg) / avg * 100;

        if (Float.isNaN(profitRate)) profitRate = 0;

        return rateFormat.format(profitRate);
    }

    public TableFragment getTableFragment() {
        return tableFragment;
    }

    public void setTableFragment(TableFragment tableFragment) {
        this.tableFragment = tableFragment;
    }
}
