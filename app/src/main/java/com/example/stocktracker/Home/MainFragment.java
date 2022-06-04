package com.example.stocktracker.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracker.ListData;
import com.example.stocktracker.Login.LoginActivity;
import com.example.stocktracker.Login.UpdateUserActivity;
import com.example.stocktracker.LoginResultActivity;
import com.example.stocktracker.PreferenceManager;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainFragment extends Fragment {

    MainAdapter adapter;

    private int custUid;
    private String nickname;
    private View view;

    public MainFragment(int cust_uid, String nickname) {
        this.custUid = cust_uid;
        this.nickname = nickname;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_main, container, false);
        adapter = new MainAdapter(custUid);

        init();
        getData();

        TextView textViewWelcome = (TextView) view.findViewById(R.id.home_welcome_text);
        textViewWelcome.setText(nickname + "님 안녕하세요!");

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.update_user: {
                        updateUser();
                        return true;
                    }

                    case R.id.delete_user: {
                        deleteUser();
                        return true;
                    }

                    case R.id.logout: {
                        logout();
                        return true;
                    }

                    default:
                        return false;
                }
            }
        });

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

    private void logout() {
        PreferenceManager.clear(getContext());

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void updateUser() {
        Intent intent = new Intent(getContext(), UpdateUserActivity.class);
        intent.putExtra("uid", custUid);
        startActivity(intent);
    }

    private void deleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("알림");
        builder.setMessage("탈퇴하시겠습니까?");
        builder.setNegativeButton("취소", null);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PreferenceManager.clear(getContext());
                deleteCust();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        builder.create();
        builder.show();
    }

    private void deleteCust() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<com.example.stocktracker.Data> call = networkService.deleteUser(custUid);

        call.enqueue(new Callback<com.example.stocktracker.Data>() {
            @Override
            public void onResponse(Call<com.example.stocktracker.Data> call, Response<com.example.stocktracker.Data> response) {
                Log.d("retrofit", "Delete Cust fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    com.example.stocktracker.Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    if ("000".equals(data.getResponse_cd())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("알림")
                                .setMessage(data.getResponse_msg())
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<com.example.stocktracker.Data> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}