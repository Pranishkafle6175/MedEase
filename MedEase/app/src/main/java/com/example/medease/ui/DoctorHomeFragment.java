package com.example.medease.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.medease.AddProducts;
import com.example.medease.MainActivity;
import com.example.medease.Model.Doctors;
import com.example.medease.R;
import com.example.medease.databinding.FragmentDoctorHomeBinding;
import com.example.medease.databinding.FragmentHomeBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class DoctorHomeFragment extends Fragment {
    FragmentDoctorHomeBinding binding;
    private int mYear, mMonth, mDay, mHour, mMinute;
    ArrayList<String> datelist ;

    private Chip mSelectedChip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDoctorHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        datelist= new ArrayList<>();

        getUserData();

        // Get Current Date
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        binding.btnDate.setOnClickListener(v -> {

            Log.i("Button Pressed","Button Pressed");

            //The month value returned by the DatePickerDialog is zero-based, which means that January is represented by 0
            //so we increment monthofYear by 1

            DatePickerDialog datePickerDialog = new DatePickerDialog(DoctorHomeFragment.this.getActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> binding.inDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        });
        binding.btnTime.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            mHour = c1.get(Calendar.HOUR_OF_DAY);
            mMinute = c1.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(DoctorHomeFragment.this.getActivity(),
                    (view12, hourOfDay, minute) -> binding.inTime.setText(hourOfDay + ":" + minute), mHour, mMinute, true);
            timePickerDialog.show();



        });

        binding.enterbutton.setOnClickListener(v -> {
            if(TextUtils.isEmpty(binding.inDate.getText().toString()) || TextUtils.isEmpty(binding.inDate.getText().toString())){



            }else {
                Log.i("time",binding.inTime.getText().toString());

                FirebaseDatabase.getInstance().getReference().child("AppointmentModel").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(binding.inDate.getText().toString())
                        .child(binding.inTime.getText().toString()).setValue("null");
            }

            binding.inDate.setText("");
            binding.inTime.setText("");
            getAppointmentTime();
        });

        binding.calendertextview.setText(mDay + "-" + (mMonth +1) + "-" + mYear);

        binding.calendarimage.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(DoctorHomeFragment.this.getActivity(),
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        binding.calendertextview.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        getAppointmentTime();
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        });

        getAppointmentTime();


        return root;
    }

    private void getUserData() {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctors doctors = snapshot.getValue(Doctors.class);
                        binding.username.setText(doctors.getUsername());
                        binding.userfield.setText(doctors.getSpeciality());

                        if(snapshot.child("Image").getValue() != null){
                            Picasso.get().load(snapshot.child("Image").getValue().toString()).into(binding.userimage);
                        }else{
                            Log.i("UserImage Null","Doctor AppointmentModel");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getAppointmentTime() {

        datelist.clear();
        String date = binding.calendertextview.getText().toString();
        Log.i("Date",date);

        FirebaseDatabase.getInstance().getReference().child("AppointmentModel").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(binding.calendertextview.getText().toString()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                            Log.i("Key",dataSnapshot.getKey());
                            datelist.add(dataSnapshot.getKey());
                        }
                        displayNamesInChips(datelist);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void displayNamesInChips(List<String> names) {
        binding.chipGroup.setSingleSelection(true);
        binding.chipGroup.removeAllViews();

        Log.i("Method CAlled", "called");

        for (String name : names) {
            Chip chip = new Chip(DoctorHomeFragment.this.getActivity());
            chip.setText(name);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setChipBackgroundColorResource(android.R.color.holo_purple);

            chip.setOnClickListener(v -> {
                Chip selectedChip = (Chip) v;
                if (selectedChip == mSelectedChip) {
                    // The same chip was selected, unselect it
                    mSelectedChip.setChecked(false);
                    mSelectedChip.setChipBackgroundColorResource(android.R.color.holo_red_light);
                    mSelectedChip = null;
                } else {
                    // A new chip was selected, unselect the previous one and select the new one
                    if (mSelectedChip != null) {
                        mSelectedChip.setChecked(false);
                        mSelectedChip.setChipBackgroundColorResource(android.R.color.holo_red_light);
                    }
                    mSelectedChip = selectedChip;
                    mSelectedChip.setChipBackgroundColorResource(R.color.lavender);
                }
            });

            binding.chipGroup.addView(chip);
        }

    }
}