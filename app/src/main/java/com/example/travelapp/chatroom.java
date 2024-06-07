package com.example.travelapp;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class chatroom extends AppCompatActivity {
    private String reviewerId;
    private String senderRoom,recieverRoom;
    FirebaseDatabase database;
    FirebaseAuth firebaseAuth;
    private String loggedInUserId;
    RecyclerView mmessangesAdpter;
    ArrayList<messageModel> messagessArraylist;
    messageAdapter messageAdapter;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        final EditText messageEditText = findViewById(R.id.messageEditText);

        Button sendButton = findViewById(R.id.sendButton);
        mmessangesAdpter = findViewById(R.id.messages);
        messagessArraylist = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        mmessangesAdpter.setLayoutManager(linearLayoutManager);
        messageAdapter = new messageAdapter(chatroom.this, messagessArraylist);
        mmessangesAdpter.setAdapter(messageAdapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            reviewerId = extras.getString("reviewerId");

        }
        FirebaseApp.initializeApp(this);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance();
        if (currentUser != null ) {
            loggedInUserId = currentUser.getUid();
        }
        else Toast.makeText(this, "please login first", Toast.LENGTH_SHORT).show();

        Log.d("reviewer id", reviewerId);
        Log.d("sender id", loggedInUserId);


        recieverRoom  = loggedInUserId+reviewerId;
         senderRoom = reviewerId + loggedInUserId;



        DatabaseReference reference = database.getReference().child("user").child(loggedInUserId);
         DatabaseReference chatreference =  database.getReference().child("chats").child(senderRoom).child("messages");

           chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagessArraylist.clear();

                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    messageModel messages = dataSnapshot.getValue(messageModel.class);
                   // Log.d("messages", messages.getMessage());

                     messagessArraylist.add(messages);
                }

                messageAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



         reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messageEditText.getText().toString();
                //Log.d("your message:", message);
                if(message.isEmpty()){

                    Toast.makeText(chatroom.this,"Enter the message ", Toast.LENGTH_SHORT).show();
                }

                messageEditText.setText("");
                Date date = new Date();
                messageModel messages = new messageModel(message,loggedInUserId,date.getTime());
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats").child(senderRoom).child("messages")
                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {

                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats").child(recieverRoom).child("messages")
                                        .push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                            }
                                        });




                            }
                        });






            }
        });





    }
}

