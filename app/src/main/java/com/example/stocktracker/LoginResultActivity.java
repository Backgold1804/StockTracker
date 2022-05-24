package com.example.stocktracker;

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
import com.example.stocktracker.Login.LoginActivity;
import com.example.stocktracker.TableFragment.TableSetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginResultActivity extends AppCompatActivity {

    int custUid;
    Button buttonLogout;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        Intent intent = getIntent();
        custUid = intent.getExtras().getInt("cust_uid");

        Bundle bundle = intent.getExtras();

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

        /* Bottom Navigation */
        bottomNavigationView = findViewById(R.id.bottomNavi);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new Main_Fragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId() ) {
                    case R.id.home_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new Main_Fragment()).commit();
                        break;
                    case R.id.table_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new TableSetFragment(custUid)).commit();
                        break;
                    case R.id.friend_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FriendFragment()).commit();
                        break;
                }
                return true;
            }
        });
        /* Bottom Navigation */

    }
}