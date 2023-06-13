package com.example.medease.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Model.AppointmentModel;
import com.example.medease.Model.Doctors;
import com.example.medease.Model.NormalUsers;
import com.example.medease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyPastAppointmentAdapter extends RecyclerView.Adapter<MyPastAppointmentAdapter.ViewHolder> {

    Context context;
    List<AppointmentModel> appointmentModel;

    public MyPastAppointmentAdapter(Context context, List<AppointmentModel> appointmentModel) {
        this.context= context;
        this.appointmentModel = appointmentModel;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userimage;
        TextView usernametextview;
        TextView chatuserfield;
        TextView chatuserexperience;
        TextView chatuserprice;
        TextView timetextview;
        TextView datetextview;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.userlistimage);
            usernametextview = itemView.findViewById(R.id.userlistusernametextview);
            chatuserfield = itemView.findViewById(R.id.chatuserfield);
            chatuserexperience = itemView.findViewById(R.id.chatuserexperience);
            chatuserprice = itemView.findViewById(R.id.chatuserprice);
            timetextview= itemView.findViewById(R.id.time);
            datetextview = itemView.findViewById(R.id.date);
        }
    }


    @NonNull
    @Override
    public MyPastAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pastappointment, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPastAppointmentAdapter.ViewHolder holder, int position) {

        AppointmentModel data = appointmentModel.get(position);

        holder.timetextview.setText(data.getAppointmentTime());
        holder.datetextview.setText(data.appointmentDate);
        Log.i("Doctor id ",data.getDoctorId());
        Log.i("Patient id ",data.getPatientId());

        getUserType(data.getDoctorId(),data.getPatientId(),holder);




    }

    private void getUserType(String doctorId, String patientId, ViewHolder holder) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        usersRef.child("Users").child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    //getInfo("Doctor",patientId);
                    getPatientData(patientId,holder);
                    Log.i("My PAst Appointment patientid",patientId);
                } else {
                    // The current user is not a doctor, so check if they are a normal user
                    usersRef.child("Users").child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {

                                //getInfo("NormalUsers",doctorId);
                                getDoctorData(doctorId,holder);
                                Log.i("My PAst Appointment patientid",doctorId);

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

//    private void getInfo(String usertype,String userid) {
//
//        if(usertype.equals("Doctor")){
//
//            getPatientData(userid);
//
//        } else if (usertype.equals("NormalUsers")) {
//            getDoctorData(userid);
//        }
//

//        FirebaseDatabase.getInstance().getReference().child("Users").child(usertype).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if(usertype.equals("Doctor")){
//
//                            Doctors doctors = snapshot.getValue(Doctors.class);
//                            usernametextview.setText(doctors.getUsername());
//                            if(snapshot.child("Image").getValue() != null){
//                                Picasso.get().load(snapshot.child("Image").getValue().toString()).into(userimage);
//                            }
//
//                        } else if (usertype.equals("NormalUsers")) {
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
  //  }

    private void getDoctorData(String userid, ViewHolder holder) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor")
                .child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Doctors doctors = snapshot.getValue(Doctors.class);
                        holder.usernametextview.setText(doctors.getUsername());
                        holder.chatuserexperience.setText(doctors.getExperience());
                        holder.chatuserfield.setText(doctors.getSpeciality());



                        Picasso.get().load(snapshot.child("Image").getValue().toString()).into(holder.userimage);
                        
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void getPatientData(String userid, ViewHolder holder) {
        FirebaseDatabase.getInstance().getReference().child("Users").child("NormalUsers")
                .child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        NormalUsers user = snapshot.getValue(NormalUsers.class);
                        holder.usernametextview.setText(user.getUsername());
                        holder.chatuserexperience.setText(user.getAge());
                        holder.chatuserfield.setText(user.getMobileNo());
                        Picasso.get().load(user.getImage()).into(holder.userimage);

                        holder.chatuserprice.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        Log.i("AppointmentModel Size", String.valueOf(appointmentModel.size()));
        return appointmentModel.size();
    }


}
