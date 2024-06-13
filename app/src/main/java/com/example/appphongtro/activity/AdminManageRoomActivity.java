package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.CursorWindow;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ManageRoomAdapter;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.navigation.AdminNavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class AdminManageRoomActivity extends AppCompatActivity {

    RecyclerView recycler_list_room;
    ManageRoomAdapter manageRoomAdapter;
    RoomTbl roomTbl;

    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage_room);
        recycler_list_room = findViewById(R.id.recycler_list_room);
        roomTbl = new RoomTbl(this);
        bottom_navigation = findViewById(R.id.bottom_navigation1);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottom_navigation.setSelectedItemId(R.id.manageRoom);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> AdminNavigationHelper.redirectPage(this, item));

        displayRoom();
        recycler_list_room.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }


    private void displayRoom() {
        ArrayList<Room> lstRoom = roomTbl.getAllRoomAdmin();
        manageRoomAdapter = new ManageRoomAdapter(this, lstRoom, R.layout.single_room);
        recycler_list_room.setAdapter(manageRoomAdapter);
    }
}