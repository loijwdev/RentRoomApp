package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HistoryFindTbl {
    private DatabaseHelper databaseHelper;

    public HistoryFindTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public void insertSearchHistory(long userId, String searchCriteria) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", userId);
        contentValues.put("search_criteria", searchCriteria);
        contentValues.put("timestamp", System.currentTimeMillis()); // You can customize the timestamp as needed
        db.insert(DatabaseHelper.TABLE_HISTORY_FIND, null, contentValues);
        db.close();
    }


    @SuppressLint("Range")
    public String getLastSearchCriteria(long userId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT search_criteria FROM " + DatabaseHelper.TABLE_HISTORY_FIND +
                " WHERE user_id = ? ORDER BY timestamp DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        String searchCriteria = null;
        if (cursor.moveToFirst()) {
            searchCriteria = cursor.getString(cursor.getColumnIndex("search_criteria"));
        }

        cursor.close();
        db.close();
        return searchCriteria;
    }

}
