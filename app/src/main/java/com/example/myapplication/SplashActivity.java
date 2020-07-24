package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        System.out.println(user);
        if (user != null) {
            // User is signed in
            // Start home activity
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
        } else {
            // No user is signed in
            // start login activity
            startActivity(new Intent(SplashActivity.this, Login.class));
        }

        // close splash activity
        finish();
    }
}
