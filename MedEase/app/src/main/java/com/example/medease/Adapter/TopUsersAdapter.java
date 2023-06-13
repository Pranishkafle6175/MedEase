package com.example.medease.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Model.Doctors;
import com.example.medease.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopUsersAdapter extends RecyclerView.Adapter<TopUsersAdapter.ViewHolder> {

    Context context;
    List<Doctors> topUsers;
    public FirebaseUser firebaseUser;

    public TopUsersAdapter(Context context , List<Doctors> topUsers) {
        this.context = context;
        this.topUsers= topUsers;
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
    public TopUsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatindividualuseractivity, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TopUsersAdapter.ViewHolder holder, int position) {

        Doctors doctors= topUsers.get(position);
        Log.i("TopUserAdapter",doctors.getUsername());
        holder.username.setText(doctors.getUsername());
        holder.doctorfieldname.setText(doctors.getSpeciality());
        holder.experience.setText(doctors.getExperience());
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor").child(doctors.getId()).child("Image");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String imageUrl = dataSnapshot.getValue(String.class);
                if(imageUrl != null){
                    Picasso.get().load(imageUrl).into(holder.userimage);

                }else{
                    Log.i("Image Url Empty","Top UserAdapter");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // handle error
            }
        });

        checkLike(holder.like,doctors.getId());
        nooflike(holder.nooflikes,doctors.getId());

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
        Log.i("Size topUSers", String.valueOf(topUsers.size()));
        return topUsers.size();
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
