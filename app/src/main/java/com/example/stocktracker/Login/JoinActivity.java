package com.example.stocktracker.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.stocktracker.Data;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {

    EditText editTextId, editTextPassword, editTextCheckText, editTextPhone, editTextFindId, editTextNickname;
    Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        buttonJoin = findViewById(R.id.join_btn);

        editTextId = (EditText) findViewById(R.id.id);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextCheckText = (EditText) findViewById(R.id.password_check);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextFindId = (EditText) findViewById(R.id.find_id_phrase);
        editTextNickname = (EditText) findViewById(R.id.nickname);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordCheck = editTextCheckText.getText().toString();
                String phone = editTextPhone.getText().toString();
                String findId = editTextFindId.getText().toString();
                String nickname = editTextNickname.getText().toString();

                if (id.trim().length() == 0 || password.trim().length() == 0 || passwordCheck.trim().length() == 0 || phone.trim().length() == 0 || findId.trim().length() == 0 || nickname.trim().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    builder.setTitle("알림")
                            .setMessage("정보를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    if (password.equals(passwordCheck)) {
                        JoinResponse(id, password, phone, findId, nickname);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage("비밀번호를 확인해주세요.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }
        });
    }

    public void JoinResponse(String id, String password, String phone, String findId, String nickname) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.setUser(id, password, phone, findId, nickname);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Join Data fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());
                    if ("000".equals(data.getResponse_cd())) {
                        Toast.makeText(getApplicationContext(), data.getResponse_msg(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(JoinActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                        builder.setTitle("알림")
                                .setMessage(data.getResponse_msg())
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Log.d("OnResponse", "실패");
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {
        View focusView = getCurrentFocus();
        if (focusView != null) {
            Rect rect = new Rect();
            focusView.getGlobalVisibleRect(rect);
            int x = (int) e.getX(), y = (int) e.getY();
            if (!rect.contains(x, y)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
                }
                focusView.clearFocus();
            }
        }
        return super.dispatchTouchEvent(e);
    }
}