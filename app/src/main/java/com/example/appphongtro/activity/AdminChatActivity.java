package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.UserMessageAdapter;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.User;
import com.example.appphongtro.navigation.AdminNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AdminChatActivity extends AppCompatActivity {

    RecyclerView recycler_list_user_chat;
    UserMessageAdapter userMessageAdapter;
    UserTbl userTbl;
    private BottomNavigationView bottom_navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        userTbl = new UserTbl(this);
        recycler_list_user_chat = findViewById(R.id.recycler_list_user_chat);
        bottom_navigation = findViewById(R.id.bottom_navigation1);
        bottom_navigation.setSelectedItemId(R.id.manageChat);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> AdminNavigationHelper.redirectPage(this, item));
        lstUserChat();
        recycler_list_user_chat.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private void lstUserChat() {
        List<User> users = userTbl.getAllUsersExceptAdmin();
        userMessageAdapter = new UserMessageAdapter(this, users, R.layout.single_room);
        recycler_list_user_chat.setAdapter(userMessageAdapter);
    }

}