package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appphongtro.model.Review;

public class ReviewTbl {

    private DatabaseHelper databaseHelper;

    public ReviewTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertReview(Review review) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("room_id", review.getRoom_id());
        values.put("rating", review.getRating());
        values.put("review_text", review.getReview_text());
        values.put("tenant_id", review.getTenant_id());
        long id = db.insert(DatabaseHelper.TABLE_REVIEW, null, values);
        db.close();

        return id;
    }

    public Cursor getReviewForRoom(int room_id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT " +
                DatabaseHelper.TABLE_USERS + ".avatar AS imgUser, " +
                DatabaseHelper.TABLE_USERS + ".fullName AS nameUser, " +
                DatabaseHelper.TABLE_REVIEW + ".rating AS rating, " +
                DatabaseHelper.TABLE_REVIEW + ".review_text AS  review_text , " +
                DatabaseHelper.TABLE_REVIEW + ".id AS reviewId " +
                "FROM " + DatabaseHelper.TABLE_REVIEW +
                " JOIN " + DatabaseHelper.TABLE_USERS +
                " ON " + DatabaseHelper.TABLE_REVIEW + ".tenant_id = " + DatabaseHelper.TABLE_USERS + ".id " +
                "WHERE " + DatabaseHelper.TABLE_REVIEW + ".room_id = " + room_id;

        Cursor cursor = db.rawQuery(query, null);
        // Trả về con trỏ
        return cursor;
    }

    @SuppressLint("Range")
    public float getAverageRatingForRoom(int room_id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT AVG(" + DatabaseHelper.TABLE_REVIEW + ".rating) AS averageRating " +
                "FROM " + DatabaseHelper.TABLE_REVIEW +
                " WHERE " + DatabaseHelper.TABLE_REVIEW + ".room_id = " + room_id;

        Cursor cursor = db.rawQuery(query, null);

        float averageRating = 0.0f;
        if (cursor != null && cursor.moveToFirst()) {
            averageRating = cursor.getFloat(cursor.getColumnIndex("averageRating"));
            cursor.close();
        }
        return averageRating;
    }

    @SuppressLint("Range")
    public boolean hasUserReviewedRoom(int room_id, int tenant_id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT COUNT(*) AS count FROM " + DatabaseHelper.TABLE_REVIEW +
                " WHERE " + DatabaseHelper.TABLE_REVIEW + ".room_id = ? AND " +
                DatabaseHelper.TABLE_REVIEW + ".tenant_id = ?";
        System.out.println(query);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(room_id), String.valueOf(tenant_id)});

        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(cursor.getColumnIndex("count"));
            cursor.close();
        }

        return count > 0;
    }

}


