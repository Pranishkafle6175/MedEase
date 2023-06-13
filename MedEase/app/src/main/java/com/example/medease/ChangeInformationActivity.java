package com.example.medease;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.medease.Model.NormalUsers;
import com.example.medease.databinding.ActivityChangeInformationBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class ChangeInformationActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageRef;
    ActivityChangeInformationBinding binding;
    ActivityResultLauncher<Intent> activityResultLauncher;
    String imageUrl;
    Uri imageUri;
    ProgressDialog progressDialog;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInformationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        progressDialog = new ProgressDialog(ChangeInformationActivity.this);
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
                    imageUri = result.getData().getData();
                    binding.profilePic.setImageURI(imageUri);
                }
            }
        });

        loadData();


        binding.profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                activityResultLauncher.launch(gallery);
            }
        });

        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("Confirm");
                builder.setMessage("Are you sure?");

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog

                        makeChanges();
                        startActivity(new Intent(ChangeInformationActivity.this, loginpage.class));
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
        });
    }

    private void makeChanges() {
        if(imageUri == null){
            databaseReference.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        databaseReference.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("Username").setValue(binding.name.getText().toString());
                                snapshot.getRef().child("MobileNo").setValue(binding.number.getText().toString());
                                snapshot.getRef().child("Age").setValue(binding.age.getText().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } else {
                        // The current user is not a doctor, so check if they are a normal user
                        databaseReference.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                snapshot.getRef().child("Username").setValue(binding.name.getText().toString());
                                snapshot.getRef().child("MobileNo").setValue(binding.number.getText().toString());
                                snapshot.getRef().child("Age").setValue(binding.age.getText().toString());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

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
            StorageReference imageRef = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());
            // Upload the image to Firebase Storage
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded image
                        imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Save the download URL in Firebase Realtime Database as the image URL for the current user
                            databaseReference.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        databaseReference.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("Image").setValue(uri.toString());
                                                snapshot.getRef().child("Username").setValue(binding.name.getText().toString());
                                                snapshot.getRef().child("MobileNo").setValue(binding.number.getText().toString());
                                                snapshot.getRef().child("Age").setValue(binding.age.getText().toString());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    } else {
                                        // The current user is not a doctor, so check if they are a normal user
                                        databaseReference.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("Image").setValue(uri.toString());
                                                snapshot.getRef().child("Username").setValue(binding.name.getText().toString());
                                                snapshot.getRef().child("MobileNo").setValue(binding.number.getText().toString());
                                                snapshot.getRef().child("Age").setValue(binding.age.getText().toString());
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle any errors here
                                }
                            });
                        });
                    })
                    .addOnFailureListener(e -> {
                    });
        }

    }

    private void loadData() {
        databaseReference.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    imageUrl = dataSnapshot.child("Image").getValue().toString();
                    Picasso.get().load(imageUrl).into(binding.profilePic);
                    binding.name.setText(dataSnapshot.child("Username").getValue().toString());
                    binding.number.setText(dataSnapshot.child("MobileNo").getValue().toString());
                    binding.age.setText(dataSnapshot.child("Age").getValue().toString());
                } else {
                    // The current user is not a doctor, so check if they are a normal user
                    databaseReference.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                NormalUsers normalUser = dataSnapshot.getValue(NormalUsers.class);
                                Picasso.get().load(normalUser.getImage()).into(binding.profilePic);
                                binding.name.setText(normalUser.getUsername());
                                binding.number.setText(normalUser.getMobileNo());
                                binding.age.setText(dataSnapshot.child("Age").getValue().toString());
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
    }
}