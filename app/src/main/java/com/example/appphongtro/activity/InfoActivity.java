package com.example.appphongtro.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appphongtro.R;
import com.example.appphongtro.database.UserTbl;
import com.example.appphongtro.model.User;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class InfoActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView tv_manageAcc, list_confirm_lich_hen, list_lich_hen, log_out, list_phong_thue, list_room_owner, list_phong_datcoc;

    private BottomNavigationView bottom_navigation;

    private SessionManagement sessionManagement;
    private int user_id;
    private UserTbl userTbl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        userTbl = new UserTbl(this);
        sessionManagement = new SessionManagement(this);
        user_id = sessionManagement.getUserId();

        findId();
        redirectAllTv();
        bottom_navigation.setSelectedItemId(R.id.profile);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this,item));
        NavigationHelper.hideAddRoomMenu(this);
        showInfo();
    }

    private void showInfo() {
        User user = userTbl.getUserbyId(user_id);

        tv_manageAcc.setText(user.getFullName());
        if(user.getAvatar() != null) {
            byte [] image = user.getAvatar();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0 , image.length);
            avatar.setImageBitmap(bitmap);
        } else {
            avatar.setImageResource(R.drawable.profile);
        }

        int role = user.getRole();
        if(role == 2) {
            list_room_owner.setVisibility(View.GONE);
            list_confirm_lich_hen.setVisibility(View.GONE);
            list_phong_datcoc.setVisibility(View.GONE);
        } else {
            list_lich_hen.setVisibility(View.GONE);
            list_phong_thue.setVisibility(View.GONE);
        }
    }

    private void redirectAllTv() {

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ManageAccountActivity.class);
                startActivity(intent);
            }
        });

        tv_manageAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ManageAccountActivity.class);
                startActivity(intent);
            }
        });
        list_confirm_lich_hen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ConfirmBookRoomActivity.class);
                startActivity(intent);
            }
        });

        list_lich_hen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ListBookRoomActivity.class);
                startActivity(intent);
            }
        });

        log_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManagement.clearSession();
                Intent intent = new Intent(InfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng hoạt động hiện tại
            }
        });

        list_phong_thue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, RoomInfoTenantActivity.class);
                intent.putExtra("tenant_id", user_id);
                startActivity(intent);
            }
        });

        list_room_owner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, ListRoomOfOwnerActivity.class);
                intent.putExtra("owner_id", user_id);
                startActivity(intent);
            }
        });

        list_phong_datcoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InfoActivity.this, RoomDepositedActivity.class);
                intent.putExtra("owner_id", user_id);
                startActivity(intent);
            }
        });
    }

    private void findId() {
        avatar = findViewById(R.id.avatar);
        tv_manageAcc = findViewById(R.id.tv_manageAcc);
        list_confirm_lich_hen = findViewById(R.id.list_confirm_lich_hen);
        list_lich_hen = findViewById(R.id.list_lich_hen);
        log_out = findViewById(R.id.log_out);
        bottom_navigation = findViewById(R.id.bottom_navigation);
        list_phong_thue = findViewById(R.id.list_phong_thue);
        list_room_owner = findViewById(R.id.list_room_owner);
        list_phong_datcoc = findViewById(R.id.list_phong_datcoc);

    }
}