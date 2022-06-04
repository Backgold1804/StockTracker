package com.example.stocktracker.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import com.example.stocktracker.PreferenceManager;
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

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextID = (EditText) findViewById(R.id.id);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.login_btn);
        checkAutoLogin = (CheckBox) findViewById(R.id.auto_login);

        context = this;
//        PreferenceManager.clear(context);

        //  autoLogin을 위한 설정
        autoId = PreferenceManager.getString(context, "id");
        autoPassword = PreferenceManager.getString(context, "password");

        if ("".equals(autoId) || "".equals(autoPassword)) {

        } else {
            loginResponse();
        }

        //  Login 기능을 처리하는 Listener
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

        //  JoinActivity로 넘어가기 위한 Listener
        textViewJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });

        //  FindIDActivity로 넘어가기 위한 Method
        textViewFindID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindIdActivity.class);
                startActivity(intent);
            }
        });

        //  FindPasswordActivity로 넘어가기 위한 Method
        textViewFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    //  login시도를 하는 method
    public void loginResponse() {
        String id;
        String password;

        //  autoLogin으로 login
        if ("".equals(autoId) || "".equals(autoPassword)) {
            id = editTextID.getText().toString();
            password = editTextPassword.getText().toString();
        } else {
            id = autoId;
            password = autoPassword;
        }

        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.getUser(id, password);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                //  통신을 했다면
                Log.d("retrofit", "Login Data fetch success");

                //  통신에 성공했다면
                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + " " + data.getResponse_msg());
                    if("000".equals(data.getResponse_cd())) {
                        if (checkAutoLogin.isChecked()) {
                            PreferenceManager.setString(context, "id", id);
                            PreferenceManager.setString(context, "password", password);
                        }

                        //  cust_uid와 nickname을 저장하여 LoginResultActivity로 이동
                        Intent intent = new Intent(LoginActivity.this, LoginResultActivity.class);
                        intent.putExtra("cust_uid", Integer.parseInt(data.getDatas().get("uid").toString()));
                        intent.putExtra("nickname", data.getDatas().get("nickname").toString());
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