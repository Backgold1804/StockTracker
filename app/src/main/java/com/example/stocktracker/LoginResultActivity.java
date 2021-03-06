package com.example.stocktracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.stocktracker.FriendFragment.FriendFragment;
import com.example.stocktracker.FriendFragment.UpdateFriendFragment;
import com.example.stocktracker.Home.MainFragment;
import com.example.stocktracker.Login.LoginActivity;
import com.example.stocktracker.TableFragment.TableSetFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LoginResultActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    int custUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_result);

        //  고객 uid와 nickname 가져오기
        Intent intent = getIntent();
        custUid = intent.getExtras().getInt("cust_uid");
        String nickname = intent.getExtras().getString("nickname");

        Bundle bundle = intent.getExtras();

        /* Bottom Navigation */
        bottomNavigationView = findViewById(R.id.bottomNavi);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new MainFragment(custUid, nickname)).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch ( item.getItemId() ) {
                    case R.id.home_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MainFragment(custUid, nickname)).commit();
                        break;
                    case R.id.table_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new TableSetFragment(custUid)).commit();
                        break;
                    case R.id.friend_fragment:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FriendFragment(custUid)).commit();
                        break;
                }
                return true;
            }
        });
        /* Bottom Navigation */

    }

    //  친구 Fragment에서 기본모드와 수정모드 전환
    public void friendFragmentChange(int index) {
        if (index == 1) {
            //  수정모드 -> 기본모드
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new FriendFragment(custUid)).commit();
        } else {
            //  기본모드 -> 수정모드
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new UpdateFriendFragment(custUid)).commit();
        }
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