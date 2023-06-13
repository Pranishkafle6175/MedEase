package com.example.medease.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.medease.Adapter.ChatUserAdapter;
import com.example.medease.Adapter.DoctorListAdapter;
import com.example.medease.ChatActivity;
import com.example.medease.Model.AppointmentModel;
import com.example.medease.Model.Doctors;
import com.example.medease.databinding.FragmentFindADoctorBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class FindADoctorFragment extends Fragment {


    private FragmentFindADoctorBinding fragmentFindADoctorBinding;
    private List<Doctors> doctorsList;
    ChatUserAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        fragmentFindADoctorBinding = FragmentFindADoctorBinding.inflate(inflater, container, false);
        View root = fragmentFindADoctorBinding.getRoot();


        doctorsList= new ArrayList<>();

        adapter = new ChatUserAdapter(getActivity(),doctorsList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(FindADoctorFragment.this.getActivity());
        fragmentFindADoctorBinding.chatuserrecyclerview.setLayoutManager(linearLayoutManager);
        fragmentFindADoctorBinding.chatuserrecyclerview.setAdapter(adapter);

        getUserType();



        return root;
    }


    private void getUserType() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("My uid",uid);


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Check if the current user is a doctor
        usersRef.child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    getChatUsers("Doctor");
                } else {
                    // The current user is not a doctor, so check if they are a normal user
                    usersRef.child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                getChatUsers("NormalUsers");

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

    private void getChatUsers(String userType) {
        if(userType.equals("NormalUsers")){

            FirebaseDatabase.getInstance().getReference().child("MyAppointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                AppointmentModel appointment = dataSnapshot.getValue(AppointmentModel.class);
                                String id = appointment.getDoctorId();
                                Log.i("Find a doctor fragment ",id);

                                doctorsList.clear();
                                FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                                        .child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                Doctors doctors = snapshot.getValue(Doctors.class);
                                                Log.i("String output",doctors.getUsername());
                                                doctorsList.add(doctors);

                                                adapter.notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        } else if (userType.equals("Doctor")) {

            FirebaseDatabase.getInstance().getReference().child("MyAppointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                AppointmentModel appointment = dataSnapshot.getValue(AppointmentModel.class);
                                String id = appointment.getPatientId();

                                doctorsList.clear();
                                FirebaseDatabase.getInstance().getReference().child("Users").child("NormalUsers")
                                        .child(id).addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                                if(snapshot.exists()){
                                                    Doctors doctors = snapshot.getValue(Doctors.class);
                                                    Log.i("String output",doctors.getUsername());
                                                    doctorsList.add(doctors);
                                                }



                                                adapter.notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
//        FirebaseDatabase.getInstance().getReference().child("Users").child(userType).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                doctorsList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
//                    Doctors doctors = dataSnapshot.getValue(Doctors.class);
//                    Log.i("String output",doctors.getUsername());
//                    doctorsList.add(doctors);
//                }
//                adapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        fragmentFindADoctorBinding = null;
    }

}