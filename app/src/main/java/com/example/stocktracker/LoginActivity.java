package com.example.stocktracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    EditText editTextID, editTextPassword;
    Button buttonLogin;
    TextView textViewJoin, textViewFindID, textViewFindPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextID = (EditText) findViewById(R.id.id);
        editTextPassword = (EditText) findViewById(R.id.password);
        buttonLogin = (Button) findViewById(R.id.login_btn);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = editTextID.getText().toString();
                String password = editTextPassword.getText().toString();

                Intent intent = new Intent(LoginActivity.this, LoginResultActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("password", password);
                startActivity(intent);
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
}