package com.example.appphongtro.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.CursorWindow;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.appphongtro.R;
import com.example.appphongtro.adapter.RoomAdapter;
import com.example.appphongtro.adapter.SlideAdapter;
import com.example.appphongtro.database.ContractTbl;
import com.example.appphongtro.database.DatabaseHelper;
import com.example.appphongtro.database.HistoryFindTbl;
import com.example.appphongtro.database.RoomTbl;
import com.example.appphongtro.model.PhotoSlide;
import com.example.appphongtro.model.Room;
import com.example.appphongtro.navigation.NavigationHelper;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class DisplayRoom extends AppCompatActivity {
    private RecyclerView recyclerViewRoom;
    private DatabaseHelper databaseHelper;
    private RoomAdapter roomAdapter;

    private ViewPager2 view_pager_2;
    private CircleIndicator3 circle_indicator_3;

    private BottomNavigationView bottom_navigation;
    private RoomTbl roomTbl;

    private SessionManagement sessionManagement;
    private int user_id;

    private HistoryFindTbl historyFindTbl;
    private ContractTbl contractTbl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_room);

        databaseHelper = new DatabaseHelper(this);
        sessionManagement = new SessionManagement(this);
        user_id = sessionManagement.getUserId();
        roomTbl = new RoomTbl(this);
        historyFindTbl = new HistoryFindTbl(this);
        contractTbl = new ContractTbl(this);
        findId();
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); // the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayRoom();
        recyclerViewRoom.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        SlideAdapter slideAdapter = new SlideAdapter(this, getListSlide());
        view_pager_2.setAdapter(slideAdapter);
        circle_indicator_3.setViewPager(view_pager_2);
        bottom_navigation.setSelectedItemId(R.id.home);
        bottom_navigation.setOnNavigationItemSelectedListener(item -> NavigationHelper.redirectPage(this,item));
        NavigationHelper.hideAddRoomMenu(this);

        if (ContextCompat.checkSelfPermission(DisplayRoom.this,
                android.Manifest.permission.POST_NOTIFICATIONS) !=
        PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions (DisplayRoom.this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
        }

        checkForNewRooms();
        checkPaymentDates();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.chat_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.icChat) {
            Intent intent = new Intent(DisplayRoom.this, ChatActivity.class);
            startActivity(intent);
        }
        return true;
    }

    private void checkForNewRooms() {
        String latestSearchCriteria = historyFindTbl.getLastSearchCriteria(user_id);
        if(latestSearchCriteria == null) {
            latestSearchCriteria = "#-#";
        }
        String[] parts = latestSearchCriteria.split(" - ");
        String extractedLocation = parts.length > 0 ? parts[0].trim() : "";
        String extractedBenefit = parts.length > 1 ? parts[1].trim() : "";
        System.out.println(extractedLocation);
        System.out.println(extractedBenefit);
        // Retrieve new rooms based on the latest search criteria
        ArrayList<Room> newRooms = roomTbl.searchRooms(extractedLocation, "", extractedBenefit, "");
        // If there are new rooms, show a notification
        if (!newRooms.isEmpty()) {
            System.out.println(newRooms.size());
            Room firstRoom = newRooms.get(newRooms.size()-1); // Retrieve the first room from the ArrayList
            // Now you can access the properties of the first room as needed
            System.out.println("New Room: " + firstRoom.getName());
            // Show a notification with the details of the first room
            makeNotification(firstRoom);
        }
    }

    private void checkPaymentDates() {
        Date contractStartDate = contractTbl.getContractStartDate(user_id);
        String status = contractTbl.isRentPaid1(user_id);
        System.out.println(status);
        // Kiểm tra nếu ngày bắt đầu hợp đồng không null
        if (contractStartDate != null) {
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTime(new Date());

            // Tính toán ngày hết hạn (1 tháng sau ngày bắt đầu)
            Calendar expirationDate = Calendar.getInstance();
            expirationDate.setTime(contractStartDate);
            expirationDate.add(Calendar.MONTH, 1);
            if (currentDate.after(expirationDate) && status != null && !status.equals("Đã thanh toán")) {
                showNotification();
            }

        }
    }

    private void showNotification() {
        String channelId = "CHANNEL_ID_NOTIFICATION";
        String channelName = "Channel Name";
        String channelDescription = "Channel Description";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the channel exists, and create it if not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                channel = new NotificationChannel(channelId, channelName, importance);
                channel.setDescription(channelDescription);
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("RoomRentPro")
                .setContentText("Thông báo đã tới ngày thanh toán")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        Intent intent = new Intent(getApplicationContext(), RoomInfoTenantActivity.class);
        intent.putExtra("tenant_id",user_id);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), user_id, intent, PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);
        notificationManager.notify(0, builder.build());
    }

    public void makeNotification(Room room) {
        String channelId = "CHANNEL_ID_NOTIFICATION";
        String channelName = "Channel Name";
        String channelDescription = "Channel Description";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // Check if the channel exists, and create it if not
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                channel = new NotificationChannel(channelId, channelName, importance);
                channel.setDescription(channelDescription);
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        builder.setSmallIcon(R.drawable.logo)
                .setContentTitle("RoomRentPro")
                .setContentText("Thông báo có phòng mới")
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), DetailRoomActivity.class);
        intent.putExtra("roomId", room.getId());
        System.out.println(room.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), room.getId(), intent, PendingIntent.FLAG_MUTABLE);

        builder.setContentIntent(pendingIntent);

        notificationManager.notify(0, builder.build());
    }




    private void displayRoom() {
        ArrayList<Room> lstRoom = roomTbl.getAllRoom();

        roomAdapter = new RoomAdapter(this, lstRoom, R.layout.single_room);
        recyclerViewRoom.setAdapter(roomAdapter);
    }

    private void findId() {
        recyclerViewRoom = findViewById(R.id.recyclerViewRoom);
        view_pager_2 = findViewById(R.id.view_pager_2);
        circle_indicator_3 = findViewById(R.id.circle_indicator_3);
        bottom_navigation = findViewById(R.id.bottom_navigation);
    }

    private List<PhotoSlide> getListSlide() {
        List<PhotoSlide> lst = new ArrayList<>();
        lst.add(new PhotoSlide(R.drawable.img_1));
        lst.add(new PhotoSlide(R.drawable.img_2));
        lst.add(new PhotoSlide(R.drawable.img_3));
        lst.add(new PhotoSlide(R.drawable.img_4));
        return lst;
    }
}