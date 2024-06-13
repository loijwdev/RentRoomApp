package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.SearchResultAdapter;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.navigation.NavigationHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerSearchRoom;
    private SearchResultAdapter searchResultAdapter;
    private BottomNavigationView bottom_navigation;
    private DatabaseHelper databaseHelper;
    private RoomTbl roomTbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        databaseHelper = new DatabaseHelper(this);
        roomTbl = new RoomTbl(this);
        findId();
        displaySearchRoom();
        recyclerSearchRoom.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL, false));
        bottom_navigation.setSelectedItemId(R.id.findRoom);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this,item));
        NavigationHelper.hideAddRoomMenu(this);
    }

    private void displaySearchRoom() {
        if (getIntent().getBundleExtra("dataSearchRoom")!=null){
            Bundle bundle=getIntent().getBundleExtra("dataSearchRoom");
            String location = bundle.getString("location");
            String area = bundle.getString("area");
            String benefit = bundle.getString("benefit");
            String priceRange = bundle.getString("priceRange");

            ArrayList<Room> lstSearchRoom = roomTbl.searchRooms(location, area, benefit,priceRange);
            searchResultAdapter = new SearchResultAdapter((Context) this, lstSearchRoom, R.layout.single_room);
            recyclerSearchRoom.setAdapter(searchResultAdapter);
        }
    }


    private void findId() {
        recyclerSearchRoom = findViewById(R.id.recyclerSearchRoom);
        bottom_navigation = findViewById(R.id.bottom_navigation);
    }
}