package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;


import com.example.medease.databinding.ActivityUserregisteractivityBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class userregisteractivity extends AppCompatActivity {

    private ActivityUserregisteractivityBinding registeractivitybinding;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//      setContentView(R.layout.activity_userregisteractivity);


        registeractivitybinding = ActivityUserregisteractivityBinding.inflate(getLayoutInflater());
        View view = registeractivitybinding.getRoot();
        setContentView(view);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        registeractivitybinding.registeruserbutton.setOnClickListener(view1 -> {

            String email= registeractivitybinding.editTextEmail.getText().toString();
            String username = registeractivitybinding.editTextRegisterUsername.getText().toString();
            String age = registeractivitybinding.editTextAge.getText().toString();
            String password = registeractivitybinding.editTextPassword.getText().toString();
            String mobileno =registeractivitybinding.editTextMobile.getText().toString();
            int radioid = registeractivitybinding.radioGroupGender.getCheckedRadioButtonId();
            RadioButton radioButton = findViewById(radioid);
            String radiobuttontext = radioButton.getText().toString();
            if(TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(age) ||TextUtils.isEmpty(mobileno)){
                Toast.makeText(userregisteractivity.this, "Empty Fields", Toast.LENGTH_LONG).show();

            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                registeractivitybinding.editTextEmail.setError("Invalid EmailAddress");
            }else if (password.length() < 5){
                registeractivitybinding.editTextPassword.setError("Password must be greater than 7 digit");
                Toast.makeText(userregisteractivity.this, "Small Password", Toast.LENGTH_LONG).show();
            } else if (mobileno.length()<10) {

                registeractivitybinding.editTextMobile.setError("Mobile-No is Invalid!!");

            } else if(registeractivitybinding.radioGroupGender.getCheckedRadioButtonId()==-1)
            {
                Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
            }
            else{
                mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("Username", username);
                            map.put("email",email);
                            map.put("Id",mAuth.getCurrentUser().getUid());
                            map.put("Age",age);
                            map.put("MobileNo",mobileno);
                            map.put("Gender",radiobuttontext);
                            databaseReference.child("Users").child("NormalUsers").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(userregisteractivity.this, "Added to database ", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(userregisteractivity.this, "Registration Sucessful", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(userregisteractivity.this,ImageSetupActivity.class));

                        }else{
                            Toast.makeText(userregisteractivity.this, "Unsucessful Registration", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        });



    }
}