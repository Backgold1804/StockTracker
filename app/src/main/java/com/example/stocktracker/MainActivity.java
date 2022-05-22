package com.example.stocktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.stocktracker.FriendFragment.FriendFragment;
import com.example.stocktracker.HomeFragment.HomeFragment;
import com.example.stocktracker.TableFragment.TableAdapter;
import com.example.stocktracker.TableFragment.TableFragment;
import com.example.stocktracker.TableFragment.TableSetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Bottom Navigation */
        bottomNavigationView = findViewById(R.id.bottomNavi);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId() ) {
                    case R.id.home_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new HomeFragment()).commit();
                        break;
                    case R.id.table_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new TableSetFragment()).commit();
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
