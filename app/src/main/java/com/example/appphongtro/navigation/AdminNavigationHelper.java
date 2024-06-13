package com.example.appphongtro.navigation;

import android.app.Activity;
import android.content.Intent;
import android.view.MenuItem;

import com.example.appphongtro.R;
import com.example.appphongtro.activity.AdminChatActivity;
import com.example.appphongtro.activity.AdminManageAccActivity;
import com.example.appphongtro.activity.AdminManageRoomActivity;
import com.example.appphongtro.activity.ChatActivity;
import com.example.appphongtro.activity.DisplayRoom;
import com.example.appphongtro.activity.InfoActivity;
import com.example.appphongtro.activity.ManageAccountActivity;

public class AdminNavigationHelper {

    public static boolean redirectPage(Activity activity, MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.manageAcc) {
            Intent intent = new Intent(activity, AdminManageAccActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.manageChat) {
            Intent intent = new Intent(activity, AdminChatActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.manageRoom) {
            Intent intent = new Intent(activity, AdminManageRoomActivity.class);
            activity.startActivity(intent);
            return true;
        } else if (itemId == R.id.profile) {
            Intent intent = new Intent(activity, InfoActivity.class);
            activity.startActivity(intent);
            return true;
        } else {
            return false;
        }
    }
}
