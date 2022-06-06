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

        //  전화번호를 보기 좋게 만들기 위한 Listener
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

        //  회원가입을 했을 때 처리해주는 Listener
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextId.getText().toString();
                String password = editTextPassword.getText().toString();
                String passwordCheck = editTextCheckText.getText().toString();
                String phone = editTextPhone.getText().toString();
                String findId = editTextFindId.getText().toString();
                String nickname = editTextNickname.getText().toString();

                phone = phone.replace("-", "");

                //  정보가 들어있는지 확인
                if (id.trim().length() == 0 || password.trim().length() == 0 || passwordCheck.trim().length() == 0 || phone.trim().length() == 0 || findId.trim().length() == 0 || nickname.trim().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                    builder.setTitle("알림")
                            .setMessage("정보를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                } else {
                    //  password와 passwordcheck가 동일한지 확인
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

    //  회원가입을 처리하는 method
    public void JoinResponse(String id, String password, String phone, String findId, String nickname) {
        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.setUser(id, password, phone, findId, nickname);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                //  통신을 했을 떄
                Log.d("retrofit", "Join Data fetch success");

                //  통신을 성공했을 때
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