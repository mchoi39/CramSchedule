package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity{
    final FragmentManager manager = getSupportFragmentManager();
    final Fragment homeFragment = new Home_Fragment();
    final Fragment calendarFragment = new Calendar_Fragment();
    final Fragment userFragment = new User_Fragment();
    Fragment active = homeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView nav_bar = findViewById(R.id.bottom_navigation);
        nav_bar.setOnNavigationItemSelectedListener(navigationListener);

        manager.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();
        manager.beginTransaction().add(R.id.fragment_container, calendarFragment, "2").hide(calendarFragment).commit();
        manager.beginTransaction().add(R.id.fragment_container, userFragment, "3").hide(userFragment).commit();

    }


    private BottomNavigationView.OnNavigationItemSelectedListener navigationListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()){
                case R.id.nav_home:
                    manager.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;

                case R.id.nav_calendar:
                    manager.beginTransaction().hide(active).show(calendarFragment).commit();
                    active = calendarFragment;
                    return true;
                case R.id.nav_profile:
                    manager.beginTransaction().hide(active).show(userFragment).commit();
                    active = userFragment;
                    return true;

            }
            return false;
        }
    };
}
