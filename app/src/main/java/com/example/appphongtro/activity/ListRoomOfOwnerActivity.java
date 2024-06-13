package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.ListRoomOfOwnerAdapter;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ListRoomOfOwnerActivity extends AppCompatActivity {

    private ImageView imgOwnerOfLstRoom;
    private TextView tvNameListRoom;
    private RecyclerView recyclerViewLstRoomOwner;
    private RoomTbl roomTbl;
    private UserTbl userTbl;
    private ListRoomOfOwnerAdapter listRoomOfOwnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_room_of_owner);
        roomTbl = new RoomTbl(this);
        userTbl = new UserTbl(this);
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        findId();
        displayRoomOfOwner();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerViewLstRoomOwner.setLayoutManager(gridLayoutManager);
    }

    private void displayRoomOfOwner() {
        Intent intent = getIntent();
        int owner_id = intent.getIntExtra("owner_id", -1);
        System.out.println(owner_id);
        ArrayList<Room> lstRoomOfOwner = roomTbl.getRoomsByOwnerId(owner_id);
        User user = userTbl.getUserbyId(owner_id);
        listRoomOfOwnerAdapter = new ListRoomOfOwnerAdapter(this, lstRoomOfOwner, R.layout.single_list_room_owner);
        tvNameListRoom.setText(user.getFullName());

        if(user.getAvatar() != null) {
            byte [] image = user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            imgOwnerOfLstRoom.setImageBitmap(bitmap);
        } else {
            imgOwnerOfLstRoom.setImageResource(R.drawable.profile);
        }
        recyclerViewLstRoomOwner.setAdapter(listRoomOfOwnerAdapter);

       listRoomOfOwnerAdapter.notifyDataSetChanged();
    }

    private void findId() {
        tvNameListRoom = findViewById(R.id.tvNameListRoom);
        recyclerViewLstRoomOwner = findViewById(R.id.recyclerViewLstRoomOwner);
        imgOwnerOfLstRoom = findViewById(R.id.imgOwnerOfLstRoom);
    }
}