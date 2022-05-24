package com.example.stocktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.stocktracker.FriendFragment.FriendFragment;
import com.example.stocktracker.Home.Main_Fragment;
import com.example.stocktracker.TableFragment.TableAdapter;
import com.example.stocktracker.TableFragment.TableFragment;
import com.example.stocktracker.TableFragment.TableSetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.os.Handler;

import com.example.stocktracker.Login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
