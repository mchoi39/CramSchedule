package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterClass extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText class1, class2, class3, class4, class5, class6;
    Button finishBtn;
    ProgressBar progressBar;
    FirebaseAuth rAuth;
    FirebaseFirestore rStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_class);

        finishBtn = (Button) findViewById(R.id.FinishBtn);

        class1 = findViewById(R.id.editTextClass1);
        class2 = findViewById(R.id.editTextClass2);
        class3 = findViewById(R.id.editTextClass3);
        class4 = findViewById(R.id.editTextClass4);
        class5 = findViewById(R.id.editTextClass5);
        class6 = findViewById(R.id.editTextClass6);


        rAuth = FirebaseAuth.getInstance();
        rStore = FirebaseFirestore.getInstance(); //instantiate

        progressBar = findViewById(R.id.progressBar);

        if(rAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        //Code to register user into the firebase

        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                final String c1 = class1.getText().toString();
                final String c2 = class2.getText().toString();
                final String c3 = class3.getText().toString();
                final String c4 = class4.getText().toString();
                final String c5 = class5.getText().toString();
                final String c6 = class6.getText().toString();

                if(TextUtils.isEmpty(c1)){
                    class1.setError("Name is required.");
                    return;
                }
//                if(TextUtils.isEmpty(c2)){
//                    class2.setError("Name is required.");
//                    return;
//                }
//                if(TextUtils.isEmpty(c3)){
//                    class3.setError("Name is required.");
//                    return;
//                }
//                if(TextUtils.isEmpty(c4)){
//                    class4.setError("Name is required.");
//                    return;
//                }
//                if(TextUtils.isEmpty(c5)){
//                    class5.setError("Name is required.");
//                    return;
//                }
//                if(TextUtils.isEmpty(c6)){
//                    class6.setError("Name is required.");
//                    return;
//                }

                Intent in = getIntent();
                final String name = in.getStringExtra("name");
                final String email = in.getStringExtra("email");
                String password = in.getStringExtra("password");

                progressBar.setVisibility(View.VISIBLE);

                //register user in firebase

                rAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterClass.this, "User Created.", Toast.LENGTH_SHORT).show();
                            //store data in FireStore before going to main activity
                            userID = rAuth.getCurrentUser().getUid(); //used to get the ID of the current user

                            Map<String, Object> userInfo = new HashMap<>();

                            userInfo.put("rName", name);
                            userInfo.put("email", email);
                            DocumentReference userDocRef = rStore.collection("Users").document(userID);
                            userDocRef.set(userInfo);

                            DocumentReference documentReference = rStore.collection("Users").document(userID).collection("MyClasses").document("classes");

                            Map<String, Object> user = new HashMap<>();

                            user.put("rName", name);
                            user.put("email", email);

                            user.put("class1", c1);
                            if (c2.length() > 0) user.put("class2", c2);
                            if (c3.length() > 0) user.put("class3", c3);
                            if (c4.length() > 0) user.put("class4", c4);
                            if (c5.length() > 0) user.put("class5", c5);
                            if (c6.length() > 0) user.put("class6", c6);

                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "User profile created for " + userID);
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }else {
                            Toast.makeText(RegisterClass.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

    }
}