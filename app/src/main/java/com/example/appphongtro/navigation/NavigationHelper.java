package com.example.appphongtro.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.example.appphongtro.R;
import com.example.appphongtro.activity.ConfirmBookRoomActivity;
import com.example.appphongtro.activity.DisplayRoom;
import com.example.appphongtro.activity.FindRoomActivity;
import com.example.appphongtro.activity.InfoActivity;
import com.example.appphongtro.activity.ListFavoriteActivity;
import com.example.appphongtro.activity.MainActivity;
import com.example.appphongtro.session.SessionManagement;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {


    public static boolean redirectPage(Activity activity, MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.home) {
            Intent intent = new Intent(activity, DisplayRoom.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.addRoom ) { // Only allow addRoom for role 1
            Intent intent = new Intent(activity, MainActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.findRoom) {
            Intent intent = new Intent(activity, FindRoomActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(activity, InfoActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.lstHeart) {
            Intent intent = new Intent(activity, ListFavoriteActivity.class);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }

    public static void hideAddRoomMenu(Activity activity) {
        BottomNavigationView bottomNavigationView = activity.findViewById(R.id.bottom_navigation);
        MenuItem addRoomMenu = bottomNavigationView.getMenu().findItem(R.id.addRoom);
        SessionManagement sessionManagement = new SessionManagement(activity);
        int role = sessionManagement.getRole();
        if(role !=1) {
            addRoomMenu.setVisible(false);
        }
    }
}
