package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.appphongtro.model.PhotoReview;

import java.io.ByteArrayOutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class PhotoReviewTbl {

    private DatabaseHelper databaseHelper;
    private Context context;

    public PhotoReviewTbl(Context context) {
        this.context = context;
        databaseHelper = new DatabaseHelper(context);
    }

    public long insertImage(PhotoReview imageModel) throws SQLException {
        byte[] imageData = getBytesFromUri(imageModel.getImageUri());
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageData);
        values.put("review_id", imageModel.getReview_id());
        long id = db.insert(DatabaseHelper.TABLE_PHOTO_REVIEW, null, values);
        db.close();
        return id;
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

    private Uri getImageUriFromBitmap(Bitmap bitmap) {
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }
    @SuppressLint("Range")
    public ArrayList<byte[]> getImagesByReviewId(long reviewId) throws SQLException {
        ArrayList<byte[]> imageList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT image FROM " + DatabaseHelper.TABLE_PHOTO_REVIEW + " WHERE review_id = ?", new String[]{String.valueOf(reviewId)});

        while (cursor.moveToNext()) {
            byte[] imageData = cursor.getBlob(cursor.getColumnIndex("image"));
            imageList.add(imageData);
        }

        cursor.close();
        db.close();

        System.out.println(imageList);
        return imageList;
    }







}
