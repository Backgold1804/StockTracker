package com.example.stocktracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class LoginResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        TextView textViewGet = findViewById(R.id.textview_get);

        Intent intent = getIntent();

        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");
        String password = bundle.getString("password");

        textViewGet.setText(id + "\n"  + password);

    }
}