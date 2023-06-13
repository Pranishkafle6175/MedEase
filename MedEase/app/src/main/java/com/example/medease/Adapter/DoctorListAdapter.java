package com.example.medease.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.DoctorListActivity;
import com.example.medease.Model.Doctors;
import com.example.medease.ProfileUI;
import com.example.medease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.ViewHolder> {

    List<Doctors> doctorsList;
    Context context;
    public FirebaseUser firebaseUser;

    public DoctorListAdapter(Context context, List<Doctors> doctorsList) {
        this.context = context;
        this.doctorsList = doctorsList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView userimage;
        private TextView username;
        private TextView doctorfieldname;
        private TextView experience;
        private TextView price;
        private CardView doctorlistcardview;
        private ImageView like;
        private TextView nooflikes;





        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userimage = itemView.findViewById(R.id.userlistimage);
            username = itemView.findViewById(R.id.userlistusernametextview);
            doctorfieldname = itemView.findViewById(R.id.chatuserfield);
            experience = itemView.findViewById(R.id.chatuserexperience);
            price = itemView.findViewById(R.id.chatuserprice);
            doctorlistcardview = itemView.findViewById(R.id.doctorlistcardview);
            like = itemView.findViewById(R.id.like);

            firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
            nooflikes=itemView.findViewById(R.id.nooflike);

            like.setVisibility(View.VISIBLE);
            nooflikes.setVisibility(View.VISIBLE);

        }
    }

    @NonNull
    @Override
    public DoctorListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatindividualuseractivity, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorListAdapter.ViewHolder holder, int position) {

        Doctors doctors = doctorsList.get(position);
        holder.username.setText(doctors.getUsername());
        holder.doctorfieldname.setText(doctors.getSpeciality());
        holder.experience.setText(doctors.getExperience());

        FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor").child(doctors.getId())
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("Image").getValue() != null){
                                    Picasso.get().load(snapshot.child("Image").getValue().toString()).into(holder.userimage);
                                }else{
                                    Log.i("UserImage Null","Doctor List Adapter");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        checkLike(holder.like,doctors.getId());
        nooflike(holder.nooflikes,doctors.getId());

        holder.doctorlistcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileUI.class);
                intent.putExtra("Uid",doctors.getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            }
        });

//        holder.doctorlistcardview.setOnClickListener( v->{
//
//        });

        holder.like.setOnClickListener(v->{
            if( holder.like.getTag().equals("like")){

                Log.i("Like pressed","pressed");
                FirebaseDatabase.getInstance().getReference().child("Like").child(doctors.getId()).child(firebaseUser.getUid()).setValue(true);
                //addNotification(post.getPostid(),post.getUid());

            }else{
                Log.i("Like Notpressed","Notpressed");

                FirebaseDatabase.getInstance().getReference().child("Like").child(doctors.getId()).child(firebaseUser.getUid()).removeValue();

            }
        });

    }

    @Override
    public int getItemCount() {
        return doctorsList.size();
    }

    private void nooflike(TextView nooflikes, String id) {
        FirebaseDatabase.getInstance().getReference().child("Like")
                .child(id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String text = String.valueOf(snapshot.getChildrenCount());
                        nooflikes.setText( text);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void checkLike(ImageView like, String id) {

        FirebaseDatabase.getInstance().getReference().child("Like").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean liked= false;
                for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (dataSnapshot.getKey().equals(firebaseUser.getUid())) {
                        liked = true;
                        break;
                    }
                }
                if (liked) {
                    like.setImageResource(R.drawable.ic_customfavourite);

                    like.setTag("liked");
                } else {
                    like.setImageResource(R.drawable.ic_favourite);
                    like.setTag("like");
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}
