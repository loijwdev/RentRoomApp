package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ListAppointmentScheduleAdapter;
import com.example.appphongtro.database.AppointmentDb;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.session.SessionManagement;

public class ListBookRoomActivity extends AppCompatActivity {

    private RecyclerView recyclerBooking;
    private ListAppointmentScheduleAdapter listAppointmentScheduleAdapter;

    private DatabaseHelper databaseHelper;
    private SessionManagement sessionManagement;
    private int owner_id;

    private AppointmentDb appointmentDb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_book_room);

        databaseHelper = new DatabaseHelper(this);
        sessionManagement = new SessionManagement(this);
        appointmentDb = new AppointmentDb(this);
        owner_id = sessionManagement.getUserId();

        findId();
        listBookingRoom();

        recyclerBooking.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));

    }

    private void listBookingRoom() {
        Cursor appointmentCursor = appointmentDb.getAppointmentDetailsForTenant(owner_id);
        listAppointmentScheduleAdapter = new ListAppointmentScheduleAdapter(appointmentCursor,this,  R.layout.single_book_room);
        recyclerBooking.setAdapter(listAppointmentScheduleAdapter);
    }

    private void findId() {
        recyclerBooking = findViewById(R.id.recyclerBooking);
    }


}