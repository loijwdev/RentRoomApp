package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.UserAccountAdapter;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.User;
import com.example.appphongtro.navigation.AdminNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class AdminManageAccActivity extends AppCompatActivity {

    UserTbl userTbl;
    UserAccountAdapter userAccountAdapter;
    RecyclerView recycler_list_user;

    private BottomNavigationView bottom_navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_acc);

        userTbl = new UserTbl(this);
        recycler_list_user = findViewById(R.id.recycler_list_user);
        bottom_navigation = findViewById(R.id.bottom_navigation1);

        bottom_navigation.setSelectedItemId(R.id.manageAcc);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> AdminNavigationHelper.redirectPage(this, item));
        lstUser();
        recycler_list_user.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }

    private void lstUser() {
        List<User> users = userTbl.getAllUsersExceptAdmin();
        userAccountAdapter = new UserAccountAdapter(this, users, R.layout.single_room);
        recycler_list_user.setAdapter(userAccountAdapter);
    }

}