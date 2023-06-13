package com.example.medease;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.medease.Model.Doctors;
import com.example.medease.Model.NormalUsers;
import com.example.medease.databinding.ActivityBookAppointmentBinding;
import com.example.medease.databinding.ActivityDoctorAppointmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class BookAppointment extends AppCompatActivity {

    ActivityBookAppointmentBinding binding;
    String doctorid;
    String userid;
    String selectedChipText;
    String appointmentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_book_appointment);

        binding = ActivityBookAppointmentBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        doctorid = intent.getStringExtra("doctorid");
        userid = intent.getStringExtra("userid");
        selectedChipText= intent.getStringExtra("chiptext");
        appointmentDate= intent.getStringExtra("Date");

        Log.i("selectedchip",selectedChipText);
        Log.i("Date",appointmentDate);

        getDoctorInfo(doctorid);
        getUserinfo(userid);

        binding.esewacardview.setOnClickListener(v ->{
            Toast.makeText(this, "This feature will be available soon", Toast.LENGTH_LONG).show();
        });
        binding.khalticardview.setOnClickListener(v->{
            Toast.makeText(this, "This feature will be available soon", Toast.LENGTH_LONG).show();
        });
        binding.imaepaycardview.setOnClickListener(v ->{
            Toast.makeText(this, "This feature will be available soon", Toast.LENGTH_LONG).show();
        });
        binding.medeasepointcardview.setOnClickListener(v->{
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Appointment").child(doctorid)
                    .child(appointmentDate).child(selectedChipText);

            reference.child("Doctor_Uid").setValue(doctorid);
            reference.child("Patient_Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());

            HashMap<String,Object> map = new HashMap<>();
            map.put("doctorId", doctorid);
            map.put("patientId",FirebaseAuth.getInstance().getCurrentUser().getUid());
            map.put("appointmentDate",appointmentDate);
            map.put("appointmentTime",selectedChipText);

            FirebaseDatabase.getInstance().getReference().child("MyAppointment").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(doctorid).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("BookAppointment","My appointmentModel type");
                }
            });

            FirebaseDatabase.getInstance().getReference().child("MyAppointment").child(doctorid).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.i("BookAppointment","My appointmentModel type");
                }
            });

            if (ContextCompat.checkSelfPermission(BookAppointment.this, Manifest.permission.SEND_SMS)== PackageManager.PERMISSION_GRANTED){
                sendMessage();
            }else{
                ActivityCompat.requestPermissions(BookAppointment.this, new String[]{Manifest.permission.SEND_SMS},100);
            }
        });

    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode== 100){
            if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
                sendMessage();
            }else{
                Toast.makeText(this, "Permission not GIven", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendMessage() {

        String message = "You have sucessfully Booked your Appointment ";
        String number = "9848949773";
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> part = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(number,null,part,null,null);
        Log.i("Message ", "Message sent");

    }

    private void getUserinfo(String userid) {

        FirebaseDatabase.getInstance().getReference().child("Users").child("NormalUsers")
                .child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NormalUsers normalUsers = snapshot.getValue(NormalUsers.class);
                        if(normalUsers.getImage() != null){
                            Picasso.get().load(normalUsers.getImage()).into(binding.patientimage);
                        }

                        binding.patientname.setText(normalUsers.getUsername());
                        binding.patientmobileno.setText(normalUsers.getMobileNo());
                        binding.patientgender.setText(normalUsers.getGender());
                        binding.patientage.setText(normalUsers.getAge());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void getDoctorInfo(String doctorid) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                .child(doctorid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctors doctors = snapshot.getValue(Doctors.class);
                        if(snapshot.child("Image").exists()){
                            Picasso.get().load(snapshot.child("Image").getValue().toString()).into(binding.doctorimage);
                        }

                        binding.doctorname.setText(doctors.getUsername());
                        binding.doctorspeciality.setText(doctors.getSpeciality());
                        binding.doctorqualification.setText(doctors.getQualification());

                        if(snapshot.child("Price").exists()){
                            binding.price.setText(snapshot.child("Price").getValue().toString());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}