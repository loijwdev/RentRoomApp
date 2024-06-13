package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ListFavoriteAdapter;
import com.example.appphongtro.database.FavoriteTbl;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ListFavoriteActivity extends AppCompatActivity {

    private RecyclerView recyclerFavorite;
    private FavoriteTbl favoriteDb;
    private SessionManagement sessionManagement;
    private int user_id;
    private ListFavoriteAdapter listFavoriteAdapter;
    private BottomNavigationView bottom_navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_favorite);

        recyclerFavorite = findViewById(R.id.recyclerFavorite);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        favoriteDb = new FavoriteTbl(this);
        sessionManagement = new SessionManagement(this);
        user_id = sessionManagement.getUserId();
        listFavorite();

        recyclerFavorite.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        bottom_navigation.setSelectedItemId(R.id.lstHeart);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this,item));
        NavigationHelper.hideAddRoomMenu(this);
    }

    private void listFavorite() {
        Cursor cursorFavorite = favoriteDb.getFavoriteRoomDetails(user_id);
        listFavoriteAdapter = new ListFavoriteAdapter(cursorFavorite, this, R.layout.single_room);
        recyclerFavorite.setAdapter(listFavoriteAdapter);
    }
}