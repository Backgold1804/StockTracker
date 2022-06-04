package com.example.stocktracker.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.stocktracker.Data;
import com.example.stocktracker.LoginResultActivity;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateUserActivity extends AppCompatActivity {

    TextView textViewId;
    EditText editTextPassword, editTextPassword_check, editTextPhone, editTextFind_id, editTextNickname;
    Button buttonUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        Intent intent = getIntent();
        int uid = intent.getExtras().getInt("uid");
        buttonUpdate = (Button) findViewById(R.id.update_btn);
        textViewId = (TextView) findViewById(R.id.id);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextPassword_check = (EditText) findViewById(R.id.password_check);
        editTextPhone = (EditText) findViewById(R.id.phone);
        editTextFind_id = (EditText) findViewById(R.id.find_id_phrase);
        editTextNickname = (EditText) findViewById(R.id.nickname);

        editTextPhone.addTextChangedListener(new TextWatcher() {

            private int beforeLength = 0;
            private int afterLength = 0;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 0) {
                    return;
                }

                afterLength = s.length();

                if (beforeLength > afterLength) {
                    if (s.toString().endsWith("-")) {
                        editTextPhone.setText(s.toString().substring(0, s.length() - 1));
                    }
                } else if (beforeLength < afterLength) {
                    if (afterLength == 5 && s.toString().indexOf("-") < 0) {
                        editTextPhone.setText(s.toString().subSequence(0, 3) + "-" + s.toString().substring(3, s.length()));
                    } else if (afterLength == 10) {
                        editTextPhone.setText(s.toString().subSequence(0, 8) + "-" + s.toString().substring(8, s.length()));
                    }
                }
                editTextPhone.setSelection(editTextPhone.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //회원정보 읽기
        setUserInfo(uid);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = editTextPassword.getText().toString();
                String passwordCheck = editTextPassword_check.getText().toString();
                String phone = editTextPhone.getText().toString();
                String findId = editTextFind_id.getText().toString();
                String nickname = editTextNickname.getText().toString();

                phone = phone.replace("-", "");

                if (password.trim().length() == 0 || passwordCheck.trim().length() == 0 || phone.trim().length() == 0 || findId.trim().length() == 0 || nickname.trim().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
                    builder.setTitle("알림")
                            .setMessage("정보를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    if (password.equals(passwordCheck)) {
                        UpdateResponse(uid, password, phone, findId, nickname);
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateUserActivity.this);
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

    private void setUserInfo(int uid) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.setUser(uid);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Set User Info fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();
                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    if ("000".equals(data.getResponse_cd())) {
                        String stringPhone = data.getDatas().get("phone").toString();
                        String phone = stringPhone.substring(0, 3) + "-" + stringPhone.substring(3, 7) + "-" + stringPhone.substring(7);

                        textViewId.setText(data.getDatas().get("id").toString());
                        editTextPassword.setText(data.getDatas().get("password").toString());
                        editTextPassword_check.setText(data.getDatas().get("password").toString());
                        editTextPhone.setText(phone);
                        editTextFind_id.setText(data.getDatas().get("find_id").toString());
                        editTextNickname.setText(data.getDatas().get("nickname").toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void UpdateResponse(int uid, String password, String phone, String findId, String nickname) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.updateUser(uid, password, phone, findId, nickname);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Update User fetch success");

                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());
                    if ("000".equals(data.getResponse_cd())) {
                        Toast.makeText(getApplicationContext(), data.getResponse_msg(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(UpdateUserActivity.this, LoginResultActivity.class);
                        intent.putExtra("cust_uid", uid);
                        intent.putExtra("nickname", nickname);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

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