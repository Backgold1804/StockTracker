package com.example.stocktracker.FriendFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.stocktracker.Data;
import com.example.stocktracker.Home.DividerItemDecoration;
import com.example.stocktracker.ListData;
import com.example.stocktracker.Login.LoginActivity;
import com.example.stocktracker.Login.UpdateUserActivity;
import com.example.stocktracker.LoginResultActivity;
import com.example.stocktracker.PreferenceManager;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendFragment extends Fragment {

    FriendAdapter adapter;
    ImageButton imageButtonManage;
    int custUid;
    View view;

    public FriendFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        adapter = new FriendAdapter(custUid);

        init();
        getData();

        imageButtonManage = (ImageButton) view.findViewById(R.id.manage_friend_button);

        //  친구 정보를 수정할 수 있는 Fragment로 이동하는 Listener
        imageButtonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginResultActivity) getActivity()).friendFragmentChange(2);
            }
        });

        //  하단 toolbar
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
        RecyclerView recyclerView = view.findViewById(R.id.friend_status);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    //  data를 가져옴
    private void getData() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectFriend(custUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                //  통신 했을 때
                Log.d("retrofit", "Select Friend fetch success");

                //  통신에 성공했을 때
                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    if ("000".equals(data.getResponse_cd())) {
                        for (Map map : data.getDatas()) {
                            FriendData itemData = new FriendData();
                            itemData.setNickname(map.get("nickname").toString());
                            Log.d("TAG", map.get("nickname").toString());
                            adapter.addItem(itemData);
                        }
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

    //  logout method
    private void logout() {
        PreferenceManager.clear(getContext());

        Intent intent = new Intent(getContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //  회원 정보 수정 method
    private void updateUser() {
        Intent intent = new Intent(getContext(), UpdateUserActivity.class);
        intent.putExtra("uid", custUid);
        startActivity(intent);
    }

    //  회원 탈퇴 method
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

    //  회원 정보를 삭제하는 method
    private void deleteCust() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.deleteUser(custUid);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<com.example.stocktracker.Data> call, Response<Data> response) {
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