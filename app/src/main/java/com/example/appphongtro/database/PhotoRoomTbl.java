package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.example.appphongtro.model.PhotoRoom;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhotoRoomTbl {
    private DatabaseHelper databaseHelper;
    private Context context;

    public PhotoRoomTbl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertImage(PhotoRoom photoRoom) throws SQLException {
        byte[] imageData = getBytesFromUri(photoRoom.getImageRoomUri());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageData);
        values.put("room_id", photoRoom.getRoom_id());
        long id = db.insert(DatabaseHelper.TABLE_PHOTO_DETAIL_ROOM, null, values);
        db.close();
        return id;
    }

    public boolean updateImage(PhotoRoom photoRoom) throws SQLException {
        byte[] imageData = getBytesFromUri(photoRoom.getImageRoomUri());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageData);
        long result = db.update(DatabaseHelper.TABLE_PHOTO_DETAIL_ROOM, values, "room_id = ?", new String[]{String.valueOf(photoRoom.getRoom_id())});
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    private byte[] getBytesFromUri(Uri uri) throws SQLException {
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } catch (Exception e) {
            throw new SQLException("Error converting image to bytes");
        }
    }

    public boolean deleteImage(int roomId) throws SQLException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        long result = db.delete(DatabaseHelper.TABLE_PHOTO_DETAIL_ROOM, "room_id=?", new String[]{String.valueOf(roomId)});

        db.close();

        return result != -1;
    }


    @SuppressLint("Range")
    public ArrayList<byte[]> getImageDetailForRoom(int roomId) {
        ArrayList<byte[]> imageList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT image FROM " + DatabaseHelper.TABLE_PHOTO_DETAIL_ROOM + " WHERE room_id = ?", new String[]{String.valueOf(roomId)});

        while (cursor.moveToNext()) {
            byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));
            imageList.add(imageData);
        }

        cursor.close();
        db.close();
        return imageList;
    }


}
