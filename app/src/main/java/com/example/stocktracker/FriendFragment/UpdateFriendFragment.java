package com.example.stocktracker.FriendFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.stocktracker.Data;
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

public class UpdateFriendFragment extends Fragment {

    UpdateFriendAdapter adapter;
    ImageButton imageButtonManage, imageButtonAdd;
    int custUid;
    View view;

    public UpdateFriendFragment(int cust_uid) {
        this.custUid = cust_uid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_friend, container, false);
        adapter = new UpdateFriendAdapter(custUid);

        init();
        getData();

        imageButtonManage = (ImageButton) view.findViewById(R.id.manage_friend_button);
        imageButtonAdd = (ImageButton) view.findViewById(R.id.add_friend_button);

        //  ???????????? Fragment??? ???????????? Listener
        imageButtonManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LoginResultActivity) getActivity()).friendFragmentChange(1);
            }
        });

        //  ????????? ????????? ??? ?????? Listener
        imageButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriend();
            }
        });

        //  ?????? toolbar
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

    //  data??? ???????????? method
    private void getData() {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<ListData> call = networkService.selectFriend(custUid);

        call.enqueue(new Callback<ListData>() {
            @Override
            public void onResponse(Call<ListData> call, Response<ListData> response) {
                //  ???????????? ???
                Log.d("retrofit", "Select Friend fetch success");

                //  ????????? ?????? ?????? ???
                if (response.isSuccessful() && response.body() != null) {
                    ListData data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    if ("000".equals(data.getResponse_cd())) {
                        for (Map map : data.getDatas()) {
                            FriendData itemData = new FriendData();
                            itemData.setNickname(map.get("nickname").toString());
                            itemData.setFriend_uid(Integer.parseInt(map.get("friend_uid").toString()));
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

    //  ????????? ???????????? method
    private void addFriend() {
        EditText editTextAddFriend = new EditText(getContext());
        LinearLayout container = new LinearLayout(getContext());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.leftMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_internal_margin);
        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.alert_dialog_internal_margin);
        editTextAddFriend.setLayoutParams(params);
        container.addView(editTextAddFriend);

        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());

        dialog.setTitle("?????? ??????")
                .setMessage("????????? ???????????? ???????????????.")
                .setView(container)
                .setNegativeButton("??????", null)
                .setPositiveButton("??????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nickname = editTextAddFriend.getText().toString();

                        if ("".equals(nickname)) {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("??????")
                                    .setMessage("???????????? ??????????????????.")
                                    .setPositiveButton("??????", null)
                                    .create().show();
                        } else {
                            addFriend(nickname);
                        }
                    }
                });

        AlertDialog alert = dialog.create();
        alert.show();
    }

    //  ????????? ???????????? method
    private void addFriend(String nickname) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.addFriend(custUid, nickname);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                //  ?????? ?????? ???
                Log.d("retrofit", "Add Friend fetch success");

                //  ????????? ?????? ?????? ???
                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    Toast.makeText(getContext(), data.getResponse_msg(), Toast.LENGTH_LONG).show();
                    if ("000".equals(data.getResponse_cd())) {
                        FriendData friendData = new FriendData();
                        friendData.setNickname(nickname);
                        friendData.setFriend_uid(Integer.parseInt(data.getDatas().get("uid").toString()));
                        adapter.addItem(friendData);
                        adapter.notifyItemInserted(adapter.getItemCount());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
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

    //  ?????? ?????? ?????? method
    private void updateUser() {
        Intent intent = new Intent(getContext(), UpdateUserActivity.class);
        intent.putExtra("uid", custUid);
        startActivity(intent);
    }

    //  ?????? ?????? method
    private void deleteUser() {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("??????");
        builder.setMessage("?????????????????????????");
        builder.setNegativeButton("??????", null);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
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

    //  ?????? ????????? ???????????? method
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

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setTitle("??????")
                            .setMessage(data.getResponse_msg())
                            .setPositiveButton("??????", null)
                            .create()
                            .show();
                }
            }

            @Override
            public void onFailure(Call<com.example.stocktracker.Data> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}