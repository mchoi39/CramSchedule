package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {
    EditText rName, rEmail, rPassword;
    Button rRegisterBtn;
    TextView rLoginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rName = findViewById(R.id.rName);
        rEmail = findViewById(R.id.rEmail);
        rPassword = findViewById(R.id.rPassword);
        rRegisterBtn = (Button) findViewById(R.id.nextBtn);
        rLoginBtn = findViewById(R.id.rLoginBtn);

        //Code to register user into the firebase

        rRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                final String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                final String name = rName.getText().toString();

                if(TextUtils.isEmpty(name)){
                    rName.setError("Name is required.");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    rEmail.setError("Email is required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    rPassword.setError("Password is required.");
                    return;
                }

                if(password.length() < 6){
                    rPassword.setError("Password must be at least 6 characters.");
                }

                Intent in = new Intent(getApplicationContext(), RegisterClass.class);
                in.putExtra("name", name);
                in.putExtra("email", email);
                in.putExtra("password", password);
                startActivity(in);
                //startActivity(new Intent(getApplicationContext(), RegisterClass.class));
                finish();
            }

        });

        rLoginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });
    }
}
