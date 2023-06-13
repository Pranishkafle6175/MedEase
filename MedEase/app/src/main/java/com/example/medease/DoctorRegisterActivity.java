package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medease.Adapter.ChatAdapter;
import com.example.medease.Model.Chats;
import com.example.medease.databinding.ActivityDoctorRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DoctorRegisterActivity extends AppCompatActivity {

    private ActivityDoctorRegisterBinding doctorRegisterBinding;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    String speciality;
    String radiobuttontext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_doctor_register);

        doctorRegisterBinding = ActivityDoctorRegisterBinding.inflate(getLayoutInflater());
        View view = doctorRegisterBinding.getRoot();
        setContentView(view);


        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();


// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.DoctorSpeciality, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        doctorRegisterBinding.SpinnerDoctorSpeciality.setAdapter(adapter);

        doctorRegisterBinding.SpinnerDoctorSpeciality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (position > 0) {
                    speciality = parent.getItemAtPosition(position).toString();
                } else {
                    Toast.makeText(DoctorRegisterActivity.this, "INVALID SECTION", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        doctorRegisterBinding.registeruserbutton.setOnClickListener(view1 -> {

            String email = doctorRegisterBinding.editTextEmail.getText().toString();
            String username = doctorRegisterBinding.editTextRegisterUsername.getText().toString();
            String age = doctorRegisterBinding.editTextAge.getText().toString();
            String password = doctorRegisterBinding.editTextPassword.getText().toString();
            String mobileno = doctorRegisterBinding.editTextMobile.getText().toString();
            int radioid = doctorRegisterBinding.radioGroupGender.getCheckedRadioButtonId();
            String experience = doctorRegisterBinding.editTextExperience.getText().toString();
            String qualification = doctorRegisterBinding.editTextQualification.getText().toString();
            String currentpractisesite = doctorRegisterBinding.editTextPractise.getText().toString();
            String currentpractisesitelocation = doctorRegisterBinding.editTextLocation.getText().toString();


            RadioButton radioButton = findViewById(radioid);
            if (radioButton != null) {
                radiobuttontext = radioButton.getText().toString();

            }
            if (TextUtils.isEmpty(speciality) || TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(age) || TextUtils.isEmpty(mobileno) || TextUtils.isEmpty(experience) || TextUtils.isEmpty(qualification) || TextUtils.isEmpty(currentpractisesitelocation) || TextUtils.isEmpty(currentpractisesite)) {
                Toast.makeText(DoctorRegisterActivity.this, "Empty Fields", Toast.LENGTH_LONG).show();

            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                doctorRegisterBinding.editTextEmail.setError("Invalid EmailAddress");
            } else if (password.length() < 5) {
                doctorRegisterBinding.editTextPassword.setError("Password must be greater than 7 digit");
                Toast.makeText(DoctorRegisterActivity.this, "Small Password", Toast.LENGTH_LONG).show();
            } else if (mobileno.length() < 10) {

                doctorRegisterBinding.editTextMobile.setError("Mobile-No is Invalid!!");

            } else if (doctorRegisterBinding.radioGroupGender.getCheckedRadioButtonId() == -1) {
                Toast.makeText(getApplicationContext(), "Please select Gender", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("Username", username);
                            map.put("email", email);
                            map.put("Id", mAuth.getCurrentUser().getUid());
                            map.put("Age", age);
                            map.put("MobileNo", mobileno);
                            map.put("Gender", radiobuttontext);
                            map.put("Experience", experience);
                            map.put("Qualification", qualification);
                            map.put("CurrentPractiseSite", currentpractisesite);
                            map.put("CurrentPractiseSiteLocation", currentpractisesitelocation);
                            map.put("Speciality", speciality);
                            databaseReference.child("Users").child("Doctor").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Toast.makeText(DoctorRegisterActivity.this, "Added to database ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(DoctorRegisterActivity.this, "Registration Sucessful", Toast.LENGTH_LONG).show();

                            startActivity(new Intent(DoctorRegisterActivity.this, ImageSetupActivity.class));

                        } else {
                            Toast.makeText(DoctorRegisterActivity.this, "Unsucessful Registration", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }

        });
    }
}
