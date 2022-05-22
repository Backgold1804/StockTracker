package com.example.stocktracker.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.stocktracker.FriendFragment.FriendFragment;
import com.example.stocktracker.Home.Main_Fragment;
import com.example.stocktracker.R;
import com.example.stocktracker.TableFragment.TableSetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginResultActivity extends AppCompatActivity {

    Button buttonLogout;

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

        buttonLogout = (Button) findViewById(R.id.logout_btn);

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sp = getSharedPreferences("autoLogin", MODE_PRIVATE);
                SharedPreferences.Editor spEdit = sp.edit();

                spEdit.clear()
                        .commit();

                Intent i = new Intent(LoginResultActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);
            }
        });

    }
}