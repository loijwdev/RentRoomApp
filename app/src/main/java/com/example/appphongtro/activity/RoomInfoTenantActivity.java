package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ListAppointmentScheduleAdapter;
import com.example.appphongtro.adapter.RoomTenantAdapter;
import com.example.appphongtro.database.DepositTbl;
import com.example.appphongtro.session.SessionManagement;

public class RoomInfoTenantActivity extends AppCompatActivity {

    RecyclerView recycler_room_tenant;
    RoomTenantAdapter roomTenantAdapter;

    private DepositTbl depositTbl;

    int tenant_id ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_info_tenant);

        depositTbl = new DepositTbl(this);

        tenant_id = getIntent().getIntExtra("tenant_id", -1);
        System.out.println(tenant_id);
        recycler_room_tenant = findViewById(R.id.recycler_room_tenant);

        Cursor depositCursor = depositTbl.getRoomInfoForTenant(tenant_id);
        roomTenantAdapter = new RoomTenantAdapter(depositCursor,this,  R.layout.single_room_owner);
        recycler_room_tenant.setAdapter(roomTenantAdapter);

        recycler_room_tenant.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));

    }
}