package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ListImageOfRoomAdapter;
import com.example.appphongtro.database.PhotoRoomTbl;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListImageRoomActivity extends AppCompatActivity {

    private RecyclerView recycler_list_img_room;
    private ListImageOfRoomAdapter listImageOfRoomAdapter;

    private PhotoRoomTbl photoRoomTbl;
    private int room_id;
    private ArrayList<byte[]> imageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_image_room);

        photoRoomTbl = new PhotoRoomTbl(this);
        Intent intent = getIntent();
        room_id = intent.getIntExtra("room_id",-1);
        findId();
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        listImageOfRoom();
    }

    private void listImageOfRoom() {
        imageList = photoRoomTbl.getImageDetailForRoom(room_id);
        listImageOfRoomAdapter = new ListImageOfRoomAdapter(imageList);
        recycler_list_img_room.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_list_img_room.setAdapter(listImageOfRoomAdapter);
    }

    private void findId() {
        recycler_list_img_room = findViewById(R.id.recycler_list_img_room);
    }
}