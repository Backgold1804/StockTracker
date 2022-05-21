package com.example.stocktracker.Home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.stocktracker.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView RV = findViewById(R.id.RV);
//        RV.setLayoutManager(new LinearLayoutManager(this));
//        RV.setAdapter(new M1RVadapter(this, new Data())); // Data test;;

        if (findViewById(R.id.fragment_container) != null){
            Main_Fragment fragment = new Main_Fragment();
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

    }
}