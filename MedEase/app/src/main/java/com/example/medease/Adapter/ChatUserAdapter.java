package com.example.medease.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.ChatActivity;
import com.example.medease.Model.Doctors;
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

public class ChatUserAdapter extends RecyclerView.Adapter<ChatUserAdapter.ViewHolder> {

    List<Doctors> chatUserList;
    Context context;
    String userType;

    public ChatUserAdapter(Context context, List<Doctors> chatUserList) {
        this.context = context;
        this.chatUserList = chatUserList;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView userimage;
        TextView usernametextview;
        TextView chatuserfield;
        TextView chatuserexperience;
        TextView chatuserprice;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.userlistimage);
            usernametextview = itemView.findViewById(R.id.userlistusernametextview);
            chatuserfield = itemView.findViewById(R.id.chatuserfield);
            chatuserexperience = itemView.findViewById(R.id.chatuserexperience);
            chatuserprice = itemView.findViewById(R.id.chatuserprice);
        }
    }

    @NonNull
    @Override
    public ChatUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatindividualuseractivity, parent, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ChatUserAdapter.ViewHolder holder, int position) {
        Doctors doctors = chatUserList.get(position);


        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("My uid",uid);


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        // Check if the current user is a doctor
        usersRef.child("Doctor").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    holder.usernametextview.setText(doctors.getUsername());
                    holder.chatuserfield.setText(doctors.getGender());
                    holder.chatuserexperience.setText(doctors.getMobileNo());
                    FirebaseDatabase.getInstance().getReference("Users").child("NormalUsers").child(doctors.getId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("Image").getValue() != null){
                                Picasso.get().load(snapshot.child("Image").getValue().toString()).into(holder.userimage);
                            }else{
                                Log.i("UserImage Null","Doctor AppointmentModel");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(context, ChatActivity.class);
                            intent.putExtra("userid",doctors.getId());
                            intent.putExtra("usertype","Doctor");
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        }
                    });
                } else {
                    // The current user is not a doctor, so check if they are a normal user
                    usersRef.child("NormalUsers").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {

                                holder.usernametextview.setText(doctors.getUsername());
                                holder.chatuserfield.setText(doctors.getSpeciality());
                                holder.chatuserexperience.setText(doctors.getExperience());

                                FirebaseDatabase.getInstance().getReference("Users").child("Doctor").child(doctors.getId()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.child("Image").getValue() != null){
                                            Picasso.get().load(snapshot.child("Image").getValue().toString()).into(holder.userimage);
                                        }else{
                                            Log.i("UserImage Null","Doctor AppointmentModel");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                holder.itemView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(context, ChatActivity.class);
                                        intent.putExtra("userid",doctors.getId());
                                        intent.putExtra("usertype","NormalUsers");
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                });
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

    @Override
    public int getItemCount() {
        return chatUserList.size();
    }


}