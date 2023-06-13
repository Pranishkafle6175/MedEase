package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.medease.Model.Doctors;
import com.example.medease.databinding.ActivityProfileUiBinding;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileUI extends AppCompatActivity {

    EditText dateTXT;
    ImageView cal;
    private int mDate, mMonth, mYear;
    ActivityProfileUiBinding binding;
    ArrayList<String> datelist ;
    private  Chip mSelectedChip;
    String doctorid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_profile_ui);
        binding = ActivityProfileUiBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        dateTXT= findViewById(R.id.date);
        cal= findViewById(R.id.datepicker);

        datelist= new ArrayList<>();


        Intent intent = getIntent();
        doctorid = intent.getStringExtra("Uid");

        getDoctorInfo(doctorid);

        final Calendar c= Calendar.getInstance();
        mDate = c.get(Calendar.DATE);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);

        binding.date.setText(mDate + "-" + (mMonth +1) + "-" + mYear);

        cal.setOnClickListener(view12 -> {


            DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileUI.this,
                    (view1, year, monthOfYear, dayOfMonth) -> {
                        binding.date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                        getAppointmentTime();
                    }, mYear, mMonth, mDate);
            datePickerDialog.show();
        });

        getAppointmentTime();
    }

    private void getDoctorInfo(String userid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                .child(doctorid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctors doctors = snapshot.getValue(Doctors.class);
                        Log.i("Doctor name", doctors.getUsername());
                        Log.i("Image Url ", snapshot.child("Image").getValue().toString());
                        String Imageurl = snapshot.child("Image").getValue().toString();


                        if(Imageurl != null ){
                            Picasso.get().load(Imageurl).into(binding.imageView);
                        }else{
                            Toast.makeText(ProfileUI.this, "No image ", Toast.LENGTH_SHORT).show();
                        }

                        binding.doctorname.setText(doctors.getUsername());
                        binding.specialist.setText(doctors.getSpeciality());
                        binding.doctorexperience.setText(doctors.getExperience());
                        binding.doctorqualification.setText(doctors.getQualification());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getAppointmentTime() {

        datelist.clear();
        String date = binding.date.getText().toString();
        Log.i("Date",date);

        FirebaseDatabase.getInstance().getReference().child("AppointmentModel").child(doctorid)
                .child(binding.date.getText().toString()).addValueEventListener(new ValueEventListener() {
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

        Log.i("Method CAlled","called");

        for (String name : names) {
            Chip chip = new Chip(this);
            chip.setText(name);
            chip.setClickable(true);
            chip.setCheckable(true);

            Drawable background = ContextCompat.getDrawable(getApplicationContext(), R.drawable.buttonbackground);
            chip.setBackground(background);

            chip.setChipBackgroundColorResource(android.R.color.holo_red_light);

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
                    mSelectedChip.setChipBackgroundColorResource(R.color.purple_200);
                }
            });

            binding.chipGroup.addView(chip);
        }


        binding.appointmentbutton.setOnClickListener(v -> {
            if (mSelectedChip != null) {
                String selectedChipText = mSelectedChip.getText().toString();
                Log.i("Selected chip text:" , selectedChipText.toString());
                Log.i("Date AppointmentModel:" , binding.date.getText().toString());




                Intent intent = new Intent(ProfileUI.this,BookAppointment.class);
                intent.putExtra("doctorid",doctorid);
                intent.putExtra("userid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                intent.putExtra("chiptext",selectedChipText);
                intent.putExtra("Date",binding.date.getText().toString());
                startActivity(intent);


            } else {
                Log.i("Chips", "No chip selected");
            }
        });
    }
}