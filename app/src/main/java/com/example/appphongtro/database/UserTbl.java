package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appphongtro.model.User;

import java.util.ArrayList;
import java.util.List;

public class UserTbl {
    private DatabaseHelper databaseHelper;

    public UserTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public Boolean insertData(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", user.getEmail());
        values.put("username", user.getUsername());
        values.put("fullName", user.getFullName());
        values.put("phoneNumber", user.getPhoneNumber());
        values.put("password", user.getPassword());
        values.put("address", user.getAddress());
        values.put("role", user.getRole());
        values.put("isLocked", user.getIsLocked());

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DatabaseHelper.TABLE_USERS + " where email = ?", new String[]{email});
        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkUserNamePassword(String username, String password) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from " + DatabaseHelper.TABLE_USERS + " where username = ? and password = ? and isLocked = 0", new String[]{username, password});

        if(cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    @SuppressLint("Range")
    public int getOwnerIdByUsername(String username, String password) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM " + DatabaseHelper.TABLE_USERS + " WHERE username = ? and password = ?", new String[]{username, password});

        int owner_id = 0;

        if (cursor.moveToFirst()) {
            owner_id = cursor.getInt(cursor.getColumnIndex("id"));
        }

        cursor.close();
        return owner_id;
    }

    @SuppressLint("Range")
    public User getUserbyId(int userId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        User user = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE id = " + userId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            user = new User();
            user.setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            user.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            user.setRole(cursor.getInt(cursor.getColumnIndex("role")));
        }
        cursor.close();
        db.close();
        return user;
    }

    public Boolean editUser(User user) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("fullName", user.getFullName());
        cv.put("phoneNumber", user.getPhoneNumber());
        cv.put("address", user.getAddress());
        cv.put("email", user.getEmail());
        cv.put("password", user.getPassword());
        cv.put("avatar", user.getAvatar());

        long result = db.update(DatabaseHelper.TABLE_USERS, cv, "id = ?", new String[]{String.valueOf(user.getId())});
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("Range")
    public User getOwnerInfoByRoomId(int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        User user = null;

        // Sử dụng INNER JOIN để kết nối bảng ROOM và USERS thông qua owner_id
        String query = "SELECT " +
                DatabaseHelper.TABLE_ROOM + ".owner_id AS ownerId, " +
                DatabaseHelper.TABLE_USERS + ".fullName AS ownerName, " +
                DatabaseHelper.TABLE_USERS + ".avatar AS ownerAvatar ," +
                DatabaseHelper.TABLE_USERS + ".phoneNumber AS ownerPhone " +
                "FROM " + DatabaseHelper.TABLE_ROOM + " " +
                "INNER JOIN " + DatabaseHelper.TABLE_USERS + " ON " +
                DatabaseHelper.TABLE_ROOM + ".owner_id = " +
                DatabaseHelper.TABLE_USERS + ".id " +
                "WHERE " + DatabaseHelper.TABLE_ROOM + ".id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(roomId)});
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            user.setId(cursor.getInt(cursor.getColumnIndex("ownerId")));
            user.setFullName(cursor.getString(cursor.getColumnIndex("ownerName")));
            user.setAvatar(cursor.getBlob(cursor.getColumnIndex("ownerAvatar")));
            user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("ownerPhone")));
        }
        if (cursor != null) {
            cursor.close();
        }
        return user;
    }
    @SuppressLint("Range")
    public String getFullNameById(int userId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String fullName = null;

        String query = "SELECT fullName FROM " + DatabaseHelper.TABLE_USERS + " WHERE id = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor != null && cursor.moveToFirst()) {
            fullName = cursor.getString(cursor.getColumnIndex("fullName"));
        }

        if (cursor != null) {
            cursor.close();
        }

        return fullName;
    }

    @SuppressLint("Range")
    public int getRoleByUsername(String username) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int role = -1;

        String query = "SELECT role FROM " + DatabaseHelper.TABLE_USERS + " WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});

        if (cursor != null && cursor.moveToFirst()) {
            role = cursor.getInt(cursor.getColumnIndex("role"));
        }
        if (cursor != null) {
            cursor.close();
        }
        return role;
    }

    @SuppressLint("Range")
    public List<User> getAllUsersExceptAdmin() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Select all users except the one with the username "admin"
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_USERS + " WHERE username != 'admin'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setAvatar(cursor.getBlob(cursor.getColumnIndex("avatar")));
                user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                user.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
                user.setPhoneNumber(cursor.getString(cursor.getColumnIndex("phoneNumber")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setRole(cursor.getInt(cursor.getColumnIndex("role")));

                userList.add(user);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return userList;
    }
    @SuppressLint("Range")
    public void updateUserIsLocked(int userId, int isLocked) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isLocked", isLocked);

        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(userId)};

        db.update(DatabaseHelper.TABLE_USERS, values, whereClause, whereArgs);

        db.close();
    }


}
