package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.RoomDepositedAdapter;
import com.example.appphongtro.adapter.RoomTenantAdapter;
import com.example.appphongtro.database.DepositTbl;

public class RoomDepositedActivity extends AppCompatActivity {
    RecyclerView recycler_deposited;
    RoomDepositedAdapter roomDepositedAdapter;

    private DepositTbl depositTbl;

    int owner_id ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_deposited);
        depositTbl = new DepositTbl(this);

        owner_id = getIntent().getIntExtra("owner_id", -1);

        recycler_deposited = findViewById(R.id.recycler_deposited);

        Cursor depositCursor = depositTbl.getRoomsDeposited(owner_id);
        roomDepositedAdapter = new RoomDepositedAdapter(depositCursor,this,  R.layout.single_room_owner);
        recycler_deposited.setAdapter(roomDepositedAdapter);

        recycler_deposited.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));

    }
}