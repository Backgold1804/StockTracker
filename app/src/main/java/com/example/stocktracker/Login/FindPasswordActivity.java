package com.example.stocktracker.Login;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Rect;
import android.os.Bundle;
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

        buttonFindPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findPasswordResponse();
            }
        });
    }

    public void findPasswordResponse() {
        String id = editTextID.getText().toString();
        String phone = editTextPhone.getText().toString();
        String findPassword = editTextFindPassword.getText().toString();

        RetrofitService networkService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<Data> call = networkService.findPassword(id, phone, findPassword);

        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                Log.d("retrofit", "Find Password fetch success");

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