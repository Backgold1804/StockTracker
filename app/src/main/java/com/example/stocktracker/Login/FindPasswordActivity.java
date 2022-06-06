package com.example.stocktracker.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

import com.example.stocktracker.Data;
import com.example.stocktracker.R;
import com.example.stocktracker.RetrofitHelper;
import com.example.stocktracker.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPasswordActivity extends AppCompatActivity {

    EditText editTextID, editTextPhone, editTextFindPassword;
    Button buttonFindPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_password);

        buttonFindPassword = (Button) findViewById(R.id.find_password_btn);

        editTextID = (EditText) findViewById(R.id.find_password_id);
        editTextPhone = (EditText) findViewById(R.id.find_password_phone);
        editTextFindPassword = (EditText) findViewById(R.id.find_password_phrase);

        //  핸드폰번호를 보기 좋게 하기 위한 Listener
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

        buttonFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPasswordResponse();
            }
        });
    }

    //  비밀번호를 찾기 위한 method
    public void findPasswordResponse() {
        String id = editTextID.getText().toString();
        String phone = editTextPhone.getText().toString();
        String findPassword = editTextFindPassword.getText().toString();

        phone = phone.replace("-", "");

        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.findPassword(id, phone, findPassword);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                //  통신을 했을 때
                Log.d("retrofit", "Find Password fetch success");

                //  통신에 성공 했을 때
                if (response.isSuccessful() && response.body() != null) {
                    Data data = response.body();

                    Log.d("OnResponse", data.getResponse_cd() + ": " + data.getResponse_msg());

                    if ("000".equals(data.getResponse_cd())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);
                        builder.setTitle("알림")
                                .setMessage("비밀번호호는 " + data.getDatas().get("password").toString() + "입니다.")
                                .setPositiveButton("확인", null)
                                .create()
                                .show();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(FindPasswordActivity.this);
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