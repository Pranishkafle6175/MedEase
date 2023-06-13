package com.example.medease;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medease.Adapter.ChatAdapter;
import com.example.medease.Model.Chats;
import com.example.medease.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.jitsi.meet.sdk.JitsiMeet;
import org.jitsi.meet.sdk.JitsiMeetActivity;
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions;
import org.jitsi.meet.sdk.JitsiMeetOngoingConferenceService;

public class ChatActivity extends AppCompatActivity {
    ImageView userimage;
    TextView usernametextview;
    RecyclerView chatrecyclerview;
    EditText chatmessage;
    ImageView chatsendimage;
    String userid;
    String usertype;
    List<Chats> chatlist;
    ChatAdapter chatAdapter;
    RecyclerView.Adapter adapter;
    String image;
    URL serverUrl;
    String videoRoomCode = FirebaseAuth.getInstance().getUid();

    int height;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        userimage = findViewById(R.id.chatuserimage);
        usernametextview = findViewById(R.id.chatusername);
        chatrecyclerview = findViewById(R.id.chatrecyclerview);
        chatmessage = findViewById(R.id.chatedittext);
        chatsendimage = findViewById(R.id.chatsendimageview);


        findViewById(R.id.callBoxLayout).setVisibility(View.GONE);

        // Get the current LayoutParams of the RecyclerView
        ViewGroup.LayoutParams layoutParams = chatrecyclerview.getLayoutParams();
        height = layoutParams.height;
        // Set the height of the LayoutParams to MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;

        // Set the LayoutParams back to the RecyclerView
        chatrecyclerview.setLayoutParams(layoutParams);

        userid= getIntent().getStringExtra("userid");
        usertype =getIntent().getStringExtra("usertype");
        Log.i("usertype",usertype);
        Log.i("Userid",userid);





        try {
            serverUrl = new URL("https://meet.jit.si");
            JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder().setServerURL(serverUrl).build();
            JitsiMeet.setDefaultConferenceOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
        findViewById(R.id.videoCallBtn).setOnClickListener(view -> {
            HashMap<String,Object> map = new HashMap<>();
            map.put("Caller",FirebaseAuth.getInstance().getUid());
            map.put("To",userid);
            FirebaseDatabase.getInstance().getReference("VideoCall").child(userid).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    startVideoCall();
                }
            });
        });
        
        if(usertype.equals("NormalUsers")){

            FirebaseDatabase.getInstance().getReference().child("Users").child("Doctor").child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = (String) snapshot.child("Username").getValue();
                    //Log.i("Name",name);
                    usernametextview.setText(name);
                    image = (String) snapshot.child("Image").getValue();
                    if (image != null) {
                        Log.i("Image image",image);
                        Picasso.get().load(image).into(userimage);
                        chatAdapter.setUserImage(image);
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            
        } else if (usertype.equals("Doctor")) {

            FirebaseDatabase.getInstance().getReference().child("Users").child("NormalUsers").child(userid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = (String) snapshot.child("Username").getValue();
                    //Log.i("Name",name);
                    usernametextview.setText(name);
                    image = (String) snapshot.child("Image").getValue();
                    if (image != null) {
                        Log.i("Image image",image);
                        Picasso.get().load(image).into(userimage);
                        chatAdapter.setUserImage(image);
                    }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            
        }






        chatlist = new ArrayList<>();
        //Log.i("Image",image);
        chatAdapter = new ChatAdapter(ChatActivity.this,chatlist , null);
        chatrecyclerview.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ChatActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(false);
        chatrecyclerview.setLayoutManager(layoutManager);

        adapter = chatrecyclerview.getAdapter();

        getmessages();

        // put the Username in the chat




        chatsendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,Object> hashMap = new HashMap<>();
                String chat = chatmessage.getText().toString();
                hashMap.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());
                hashMap.put("receiver",userid);
                hashMap.put("message",chat);
                if(!TextUtils.isEmpty(chat)){

                    FirebaseDatabase.getInstance().getReference().child("Chats").push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ChatActivity.this, "Message Send", Toast.LENGTH_LONG).show();
                            chatmessage.setText("");
                        }
                    });

                }else{
                    chatmessage.setError("Text is Empty! Please Type...");
                }
                chatAdapter.notifyDataSetChanged();
            }

        });
        checkCall();
    }


    private void startVideoCall() {
            JitsiMeetConferenceOptions meetConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                    .setRoom(videoRoomCode)
                    .setFeatureFlag("welcomepage.enabled", false)
                    .build();
            JitsiMeetActivity.launch(ChatActivity.this,meetConferenceOptions);
    }

    private void getmessages() {
        FirebaseDatabase.getInstance().getReference().child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatlist.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Chats chats = dataSnapshot.getValue(Chats.class);
                    if(((chats.getSender().equals(userid)) && (chats.getReceiver().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))) || (((chats.getReceiver().equals(userid)) && (chats.getSender().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))))){

                        chatlist.add(chats);
                        Log.i("chats Added",chats.getMessage());
                        chatrecyclerview.scrollToPosition(adapter.getItemCount() - 1);



                    }else{
                        //                        Log.i("userid",userid);
                        //                        Log.i("My id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                        //                        Log.i("sender",chats.getSender());
                        //                        Log.i("Receiver",chats.getReceiver());
                        //                        Log.i(" No chats","no chat");
                    }
                }
                chatAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void checkCall() {
        FirebaseDatabase.getInstance().getReference().child("VideoCall").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild(FirebaseAuth.getInstance().getUid())){
                    String joinRoomCode = snapshot.child(FirebaseAuth.getInstance().getUid()).child("Caller").getValue(String.class);

                    AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
                    builder.setMessage("Someone is calling")
                            .setTitle("Videocall");

                    builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseDatabase.getInstance().getReference("VideoCall").child(FirebaseAuth.getInstance().getUid()).removeValue();
                            JitsiMeetConferenceOptions meetConferenceOptions = new JitsiMeetConferenceOptions.Builder()
                                    .setRoom(joinRoomCode)
                                    .setFeatureFlag("welcomepage.enabled", false)
                                    .build();
                            JitsiMeetActivity.launch(ChatActivity.this,meetConferenceOptions);
                        }
                    });
                    builder.setNegativeButton("Reject", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            FirebaseDatabase.getInstance().getReference("VideoCall").child(FirebaseAuth.getInstance().getUid()).removeValue();
                        }
                    });

                    builder.show();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
