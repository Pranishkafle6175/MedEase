package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;


import com.example.medease.databinding.ActivityLoginpageBinding;
import com.example.medease.ui.DoctorAppointment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class loginpage extends AppCompatActivity {
    DatabaseReference usersRef;

//    String uid;

    private ActivityLoginpageBinding loginpageBinding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this is not required if we use View Binding
//        setContentView(R.layout.activity_loginpage);
        loginpageBinding = ActivityLoginpageBinding.inflate(getLayoutInflater());
        View view = loginpageBinding.getRoot();
        setContentView(view);
        usersRef = FirebaseDatabase.getInstance().getReference();
//        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mAuth = FirebaseAuth.getInstance();


        //Log.i("username",usernametext);
        loginpageBinding.loginButton.setOnClickListener( v ->{
            String usernametext = String.valueOf(loginpageBinding.email.getText());
            String password = String.valueOf(loginpageBinding.password.getText());
            Log.i("username",usernametext);
        } );



        loginpageBinding.signupuserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginpage.this, userregisteractivity.class));
            }
        });

        loginpageBinding.signupDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(loginpage.this, DoctorRegisterActivity.class));
            }
        });

        loginpageBinding.loginButton.setOnClickListener(view1 -> {
            String email = loginpageBinding.email.getText().toString();
            String password = loginpageBinding.password.getText().toString();
            if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                Toast.makeText(loginpage.this, "Empty Columns", Toast.LENGTH_LONG).show();
            }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                Toast.makeText(loginpage.this, "Invalid Email Address", Toast.LENGTH_LONG).show();
            }
            else{
                mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){


                            usersRef.child("Users").child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {

                                        Toast.makeText(loginpage.this, "Login Sucessful", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(loginpage.this, DoctorMainActivity.class));

                                    } else {
                                        // The current user is not a doctor, so check if they are a normal user
                                        usersRef.child("Users").child("NormalUsers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {

                                                    Toast.makeText(loginpage.this, "Login Sucessful", Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(loginpage.this, MainActivity.class));

                                                } else {
                                                    // The current user is neither a doctor nor a normal user
                                                    // Do whatever you need to do here
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                // Handle any errors here
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle any errors here
                                }
                            });

                        }else{
                            Toast.makeText(loginpage.this, "Login Unsucessful", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            usersRef.child("Users").child("Doctor").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        startActivity(new Intent(loginpage.this, DoctorMainActivity.class));
                        finish();

                    } else {
                        startActivity(new Intent(loginpage.this,MainActivity.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors here
                }
            });

        }
    }
}