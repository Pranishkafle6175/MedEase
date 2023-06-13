package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.medease.databinding.ActivityChangeInformationBinding;
import com.example.medease.databinding.ActivityChangePasswordBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    @NonNull ActivityChangePasswordBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        binding.changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(binding.currentPassword.getText().toString().isEmpty()){
                    binding.currentPassword.setError("Cannot be Empty");
                } else if (binding.newPassword.getText().toString().isEmpty()) {
                    binding.newPassword.setError("Cannot be Empty");
                } else if (binding.confirmNewPassword.getText().toString().isEmpty()) {
                    binding.confirmNewPassword.setError("Cannot be Empty");
                } else if (!binding.confirmNewPassword.getText().toString().equals(binding.newPassword.getText().toString())) {
                    binding.confirmNewPassword.setError("Password do not match");

                } else{
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(firebaseUser.getEmail(), binding.currentPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                            builder.setTitle("Confirm");
                            builder.setMessage("Are you sure?");

                            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing but close the dialog
                                    firebaseUser.updatePassword(binding.confirmNewPassword.getText().toString());
                                    Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent i = new Intent(ChangePasswordActivity.this, loginpage.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    Toast.makeText(ChangePasswordActivity.this, "Please Log In Again To Continue!", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            });

                            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });

                            AlertDialog alert = builder.create();
                            alert.show();

                            }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ChangePasswordActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            binding.currentPassword.setError("Incorrect Password");
                        }
                    });
                }


            }
        });
    }
}