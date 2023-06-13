package com.example.medease;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.medease.Model.Doctors;
import com.example.medease.Model.NormalUsers;
import com.example.medease.databinding.ActivityImageSetupBinding;
import com.example.medease.databinding.ActivityLoginpageBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class ImageSetupActivity extends AppCompatActivity {

    ImageView setupimage;
    ActivityResultLauncher<Intent> activityResultLauncher;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    private ActivityImageSetupBinding binding;

    String Usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_image_setup);

        binding = ActivityImageSetupBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupimage= findViewById(R.id.setupimage);

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null){
//
//                    Uri imageUri = result.getData().getData();
//                    setupimage.setImageURI(imageUri);
//
//                    //In this case, the Intent that is returned by the shop app contains the URI of the selected image
//                    // in its data field. So when we call result.getData(), we get an Intent object that contains the image URI as its data field.
//                    //
//                    //To extract the actual URI of the selected image, we need to call getData() again on the Intent object.
//                    // This returns the Uri object that represents the image data.
//                    //
//                    //So the expression result.getData().getData() returns the Uri object that represents the selected image.
//                    // We can then convert this to a string URL using the toString() method, as shown in the updated implementation.
//
//                    //FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("image").setValue(imageUri);
//                    FirebaseDatabase.getInstance().getReference()
//                            .child("Users")
//                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                            .child("image")
//                            .setValue(imageUri.toString());
//                }
//            }
                    Uri imageUri = result.getData().getData();
                    setupimage.setImageURI(imageUri);




                    // Get a reference to the Firebase Storage location where you want to upload the image
                    StorageReference imageRef = storageRef.child("images/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());

                    // Upload the image to Firebase Storage
                    imageRef.putFile(imageUri)
                            .addOnSuccessListener(taskSnapshot -> {
                                // Get the download URL of the uploaded image
                                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                                    // Save the download URL in Firebase Realtime Database as the image URL for the current user
                                    FirebaseDatabase.getInstance().getReference()
                                            .child("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("image")
                                            .setValue(uri.toString());

                                    Log.i("ImageUri",uri.toString());



                                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    usersRef.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {

                                                FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor").child(uid).child("Image").setValue(uri.toString());

                                            } else {
                                                // The current user is not a doctor, so check if they are a normal user
                                                usersRef.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {

                                                            FirebaseDatabase.getInstance().getReference().child("Users").child("NormalUsers").child(uid).child("Image").setValue(uri.toString());



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


                                });
                                startActivity(new Intent(ImageSetupActivity.this, MainActivity.class));
                            })
                            .addOnFailureListener(e -> {
                            });
                }
            }
        });

        setupimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent(Intent.ACTION_GET_CONTENT);
                gallery.setType("image/*");
                activityResultLauncher.launch(gallery);
            }
        });

        // Getting the User Type --> Doctor or Normal User

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersRef.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    Usertype = "Doctor";
                    getInfo(Usertype);

                } else {
                    // The current user is not a doctor, so check if they are a normal user
                    usersRef.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                Usertype = "NormalUsers";
                                getInfo(Usertype);

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

    private void getInfo(String usertype) {
        FirebaseDatabase.getInstance().getReference().child("Users").child(usertype).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(usertype.equals("Doctor")){

                                Doctors doctor = snapshot.getValue(Doctors.class);
                                binding.name.setText(doctor.getUsername());
                                binding.gender.setText(doctor.getGender());
                                binding.number.setText(doctor.getMobileNo());

                            } else if (usertype.equals("NormalUsers")) {
                                NormalUsers user = snapshot.getValue(NormalUsers.class);
                                binding.name.setText(user.getUsername());
                                binding.gender.setText(user.getGender());
                                binding.number.setText(user.getMobileNo());
                            }
                        }
                    //}

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

}