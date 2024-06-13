package com.example.appphongtro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.MessageAdapter;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.Message;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends AppCompatActivity {
    CardView sendbtnn;
    EditText textmsg;
    private SessionManagement sessionManagement;
    private int senderId;
    FirebaseDatabase database;
    private RecyclerView recycler_view_chat;
    private MessageAdapter messageAdapter;
    ArrayList<Message> messagesArrayList;
    private UserTbl userTbl;
    public static String nameUserSend;
    String senderRoom, reciverRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        sessionManagement = new SessionManagement(this);
        userTbl = new UserTbl(this);
        findId();
        senderId = sessionManagement.getUserId();
        nameUserSend = userTbl.getFullNameById(senderId);
        FirebaseApp.initializeApp(this);
        database = FirebaseDatabase.getInstance();
        messagesArrayList = new ArrayList<>();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recycler_view_chat.setLayoutManager(linearLayoutManager);
        messageAdapter = new MessageAdapter(ChatActivity.this, messagesArrayList);
        recycler_view_chat.setAdapter(messageAdapter);


        int reciverId = getIntent().getIntExtra("receiver_id", -1);
        if(senderId != 4) {
            senderRoom = String.valueOf(senderId + 4);
            reciverRoom = String.valueOf(4 + senderId);
        } else {
            senderRoom = String.valueOf(senderId + reciverId);
            reciverRoom = String.valueOf(reciverId + senderId);
        }



        DatabaseReference  chatreference = database.getReference().child("chats").child("senderRoom").child(senderRoom).child("messages");


        chatreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArrayList.clear();
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Message messages = dataSnapshot.getValue(Message.class);
                    messagesArrayList.add(messages);
                }
                messageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        sendbtnn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String messageText = textmsg.getText().toString();
                if (messageText.isEmpty()) {
                    Toast.makeText(ChatActivity.this, "Nhập tin nhắn", Toast.LENGTH_SHORT).show();
                    return;
                }

                textmsg.setText("");
                Date date = new Date();

                Message messagess = new Message(messageText, senderId, date.getTime(), nameUserSend);
                database = FirebaseDatabase.getInstance();
                database.getReference().child("chats")
                        .child("senderRoom")
                        .child(senderRoom)
                        .child("messages")
                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.getReference().child("chats")
                                        .child("reciverRoom")
                                        .child(reciverRoom)
                                        .child("messages")
                                        .push().setValue(messagess).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                // Có thể thêm xử lý sau khi tin nhắn đã được gửi đến cả hai bên
                                            }
                                        });
                            }
                        });
            }
        });

    }
    private void findId() {
        sendbtnn = findViewById(R.id.sendbtnn);
        textmsg = findViewById(R.id.textmsg);
        recycler_view_chat = findViewById(R.id.recycler_view_chat);
    }
}