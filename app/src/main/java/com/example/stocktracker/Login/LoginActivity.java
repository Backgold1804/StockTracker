package com.example.stocktracker.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.example.stocktracker.Data;
import com.example.stocktracker.LoginResultActivity;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText editTextID, editTextPassword;
    Button buttonLogin;
    TextView textViewJoin, textViewFindID, textViewFindPassword;
    CheckBox checkAutoLogin;

    String autoId, autoPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextID = (EditText) findViewById(R.id.id);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.login_btn);
        checkAutoLogin = (CheckBox) findViewById(R.id.auto_login);

        SharedPreferences auto = getSharedPreferences("autoLogin", MODE_PRIVATE);
        autoId = auto.getString("id", null);
        autoPassword = auto.getString("password", null);

        if (autoId != null && autoPassword != null) {
            loginResponse();
        }

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextID.getText().toString();
                String password = editTextPassword.getText().toString();

                if (id.trim().length() == 0 || password.trim().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("알림")
                            .setMessage("정보를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    loginResponse();
                }
            }
        });

        textViewJoin = (TextView) findViewById(R.id.join);
        textViewFindID = (TextView) findViewById(R.id.find_id);
        textViewFindPassword = (TextView) findViewById(R.id.find_password);

        textViewJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        textViewFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(intent);
            }
        });

        textViewFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    public void loginResponse() {
        String id;
        String password;

        if (autoId != null && autoPassword != null) {
            id = autoId;
            password = autoPassword;
        } else {
            id = editTextID.getText().toString();
            password = editTextPassword.getText().toString();
        }

        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.getUser(id, password);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Login Data fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + " " + data.getResponse_msg());
                    if("000".equals(data.getResponse_cd())) {
                        if (checkAutoLogin.isChecked()) {
                            SharedPreferences auto = getSharedPreferences("autoLogin", MODE_PRIVATE);
                            SharedPreferences.Editor autoLoginEdit = auto.edit();
                            autoLoginEdit.putString("id", id)
                                    .putString("password", password)
                                    .commit();
                        }

                        Intent intent = new Intent(LoginActivity.this, LoginResultActivity.class);
                        intent.putExtra("cust_uid", Integer.parseInt(data.getDatas().get("uid").toString()));
                        startActivity(intent);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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