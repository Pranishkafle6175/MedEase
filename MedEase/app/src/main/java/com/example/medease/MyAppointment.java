package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.medease.Adapter.MyPastAppointmentAdapter;
import com.example.medease.Model.AppointmentModel;
import com.example.medease.databinding.ActivityMyAppointmentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyAppointment extends AppCompatActivity {

    List<AppointmentModel> appointmentModel;
    ActivityMyAppointmentBinding binding;
    MyPastAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_my_appointment);

        binding = ActivityMyAppointmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        appointmentModel = new ArrayList<>();

        adapter = new MyPastAppointmentAdapter(MyAppointment.this, appointmentModel);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyAppointment.this);
        binding.pastappointmentrecyclerview.setLayoutManager(linearLayoutManager);
        binding.pastappointmentrecyclerview.setAdapter(adapter);

        getAppointment();
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