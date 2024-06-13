package com.example.appphongtro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteTbl {
    private DatabaseHelper databaseHelper;

    public FavoriteTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long addFavorite(long userId, long roomId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("user_id", userId);
        values.put("room_id", roomId);
        return db.insert(DatabaseHelper.TABLE_FAVORITES, null, values);
    }

    public int removeFavorite(long userId, long roomId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        return db.delete(DatabaseHelper.TABLE_FAVORITES, "user_id=? AND room_id=?", new String[]{String.valueOf(userId), String.valueOf(roomId)});
    }

    public boolean isRoomInFavorites(long userId, long roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_FAVORITES +
                " WHERE user_id = " + userId +
                " AND room_id = " + roomId;
        Cursor cursor = db.rawQuery(query, null);
        boolean isFavorite = cursor.getCount() > 0;
        cursor.close();
        return isFavorite;
    }

    public Cursor getFavoriteRoomDetails(int userId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Define the columns you want to retrieve from both tables
        String[] columns = {
                DatabaseHelper.TABLE_ROOM + ".image" ,
                DatabaseHelper.TABLE_ROOM + ".price",
                DatabaseHelper.TABLE_ROOM + ".name",
                DatabaseHelper.TABLE_ROOM + ".address",
                DatabaseHelper.TABLE_ROOM + ".id as room_id"
        };

        String tables = DatabaseHelper.TABLE_FAVORITES +
                " INNER JOIN " + DatabaseHelper.TABLE_ROOM +
                " ON " + DatabaseHelper.TABLE_FAVORITES + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id";

        String selection = DatabaseHelper.TABLE_FAVORITES + ".user_id = ?";
        String[] selectionArgs = {String.valueOf(userId)};

        // Perform the query
        Cursor cursor = db.query(tables, columns, selection, selectionArgs, null, null, null);
        return cursor;
    }



}
