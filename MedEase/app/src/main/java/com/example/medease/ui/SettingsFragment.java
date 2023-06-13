package com.example.medease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.medease.ChangeInformationActivity;
import com.example.medease.ChangePasswordActivity;
import com.example.medease.Model.NormalUsers;
import com.example.medease.ReportBugActivity;
import com.example.medease.ViewOrderActivity;
import com.example.medease.databinding.FragmentSlideshowBinding;
import com.example.medease.loginpage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SettingsFragment extends Fragment {

    private FragmentSlideshowBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String uid,imageUrl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


        databaseReference.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    imageUrl = dataSnapshot.child("Image").getValue().toString();
                    Picasso.get().load(imageUrl).into(binding.profileImage);
                    binding.profileName.setText(dataSnapshot.child("Username").getValue().toString());
                    binding.profileEmail.setText(dataSnapshot.child("email").getValue().toString());
                    binding.profilePhoneNum.setText(dataSnapshot.child("MobileNo").getValue().toString());
                } else {
                    // The current user is not a doctor, so check if they are a normal user
                    databaseReference.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                NormalUsers normalUser = dataSnapshot.getValue(NormalUsers.class);
                                Log.e("PROFILE IMAGE",normalUser.getImage());
                                Picasso.get().load(normalUser.getImage()).into(binding.profileImage);
                                binding.profileEmail.setText(normalUser.getEmail());
                                binding.profileName.setText(normalUser.getUsername());
                                binding.profilePhoneNum.setText(normalUser.getMobileNo());
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


        binding.changeInformationCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsFragment.this.getActivity(), ChangeInformationActivity.class));
            }
        });

        binding.changePasswordCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsFragment.this.getActivity(), ChangePasswordActivity.class));
            }
        });

        binding.viewOrderCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsFragment.this.getActivity(), ViewOrderActivity.class));
            }
        });
binding.reportBugCard.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(SettingsFragment.this.getActivity(), ReportBugActivity.class));
    }
});

        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(SettingsFragment.this.getActivity(), loginpage.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}