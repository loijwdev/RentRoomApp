package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ViewBookRoomAdapter;
import com.example.appphongtro.database.AppointmentDb;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConfirmBookRoomActivity extends AppCompatActivity {

    private ViewBookRoomAdapter viewBookRoomAdapter;
    private RecyclerView recyclerBookRoom;
    private BottomNavigationView bottom_navigation;
    private DatabaseHelper databaseHelper;
    private SessionManagement sessionManagement;
    private int owner_id;

    private AppointmentDb appointmentDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_book_room);
        databaseHelper = new DatabaseHelper(this);
        appointmentDb = new AppointmentDb(this);
        sessionManagement = new SessionManagement(this);
        owner_id = sessionManagement.getUserId();
        findId();
        displayConfirmRoom();
        recyclerBookRoom.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        bottom_navigation.setSelectedItemId(0);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this,item));

    }

    private void displayConfirmRoom() {
        Cursor appointmentCursor = appointmentDb.getAppointmentDetailsForOwner(owner_id);
        viewBookRoomAdapter  = new ViewBookRoomAdapter((Context) this,appointmentCursor, R.layout.single_confirm_book_room);
        recyclerBookRoom.setAdapter(viewBookRoomAdapter);
    }

    private void findId() {
        recyclerBookRoom = findViewById(R.id.recyclerBookRoom);
        bottom_navigation = findViewById(R.id.bottom_navigation);

    }
}