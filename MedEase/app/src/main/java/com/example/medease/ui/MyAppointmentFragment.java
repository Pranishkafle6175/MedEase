package com.example.medease.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.medease.Adapter.MyPastAppointmentAdapter;
import com.example.medease.Model.AppointmentModel;
import com.example.medease.MyAppointment;
import com.example.medease.databinding.FragmentMyAppointmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAppointmentFragment extends Fragment {
    FragmentMyAppointmentBinding binding;
    MyPastAppointmentAdapter adapter;
    List<AppointmentModel> appointmentModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyAppointmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        appointmentModel = new ArrayList<>();

        adapter = new MyPastAppointmentAdapter(MyAppointmentFragment.this.getActivity(), appointmentModel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAppointmentFragment.this.getActivity());
        binding.pastappointmentrecyclerview.setLayoutManager(linearLayoutManager);
        binding.pastappointmentrecyclerview.setAdapter(adapter);

            getAppointment();




        return root;
    }

    private void getAppointment() {
        FirebaseDatabase.getInstance().getReference().child("MyAppointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                appointmentModel.clear();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){

                    AppointmentModel list = dataSnapshot.getValue(AppointmentModel.class);
                    appointmentModel.add(list);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}