package com.example.appphongtro.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "AppPhongTro4.db";
    public String DB_path = "/data/data/com.example.appphongtro/databases/";
    public static final String TABLE_USERS = "user";
    public static final String TABLE_ROOM = "room";
    public static final String TABLE_APPOINTMENT = "appointment";

    public static final String TABLE_REPORT = "report";

    public static final String TABLE_PHOTO_REVIEW = "photo_review";

    public static final String TABLE_REVIEW = "review";

    public static final String TABLE_PHOTO_DETAIL_ROOM = "photo_room";

    public static final String TABLE_FAVORITES = "favorite";

    public static final String TABLE_HISTORY_FIND = "history_find";
    public static final String TABLE_DEPOSIT = "deposit";
    public static final String TABLE_CONTRACTS = "contracts";
    private Context mContext;

    public DatabaseHelper(@Nullable Context context) {
        super(context, databaseName, null, 1);
        this.mContext = context;
    }

    public void copyDatabaseFromAssets() throws IOException {
        InputStream myInput = mContext.getAssets().open(databaseName);
        String outFileName = DB_path + databaseName;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table " + TABLE_USERS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, username TEXT, fullName TEXT, phoneNumber TEXT, password TEXT, address TEXT, role INTEGER, avatar blob, isLocked INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_ROOM + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "name TEXT,"
                + "description TEXT,"
                + "price REAL,"
                + "address TEXT,"
                + "area REAL,"
                + "amenities TEXT,"
                + "rules TEXT,"
                + "owner_id INTEGER,"
                + "image blob,"
                + "status INTEGER,"
                + "deposit_money REAL,"
                + "timestamp INTEGER,"
                + "electricity_money REAL,"
                + "water_money REAL,"
                + "internet_money REAL,"
                + "parking_fee TEXT,"
                + "FOREIGN KEY(owner_id) REFERENCES " + TABLE_USERS + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_APPOINTMENT + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tenant_id INTEGER,"
                + "room_id INTEGER,"
                + "appointmentDate INTEGER," // Lưu trữ thời điểm dưới dạng timestamp
                + "note TEXT,"
                + "status TEXT,"
                + "hasVisited INTEGER,"
                + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_REPORT + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "reporter_id INTEGER,"
                + "room_id INTEGER,"
                + "reason TEXT,"
                + "FOREIGN KEY(reporter_id) REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_REVIEW + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_id INTEGER,"
                + "rating REAL,"
                + "review_text TEXT,"
                + "tenant_id INTEGER,"
                + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_PHOTO_REVIEW + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "review_id INTEGER,"
                + "image BLOB,"
                + "FOREIGN KEY(review_id) REFERENCES " + TABLE_REVIEW + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_PHOTO_DETAIL_ROOM + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_id INTEGER,"
                + "image BLOB,"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "room_id INTEGER,"
                + "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_HISTORY_FIND + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "user_id INTEGER,"
                + "search_criteria TEXT,"
                + "timestamp INTEGER,"
                + "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(id))");

        db.execSQL("CREATE TABLE " + TABLE_DEPOSIT + "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "room_id INTEGER,"
                + "tenant_id INTEGER,"
                + "amount REAL,"
                + "timestamp INTEGER,"
                + "status TEXT,"
                + "transactionMomoToken TEXT,"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id),"
                + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USERS + "(id))");

        db.execSQL("CREATE TABLE " +  TABLE_CONTRACTS +  "("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "tenant_id INTEGER,"
                + "room_id INTEGER,"
                + "start_date DATE,"
                + "end_date DATE,"
                + "deposit REAL,"
                + "rent REAL,"
                + "other_fees REAL,"
                + "status TEXT,"
                + "isPaidRent TEXT,"
                + "FOREIGN KEY(tenant_id) REFERENCES " + TABLE_USERS + "(id),"
                + "FOREIGN KEY(room_id) REFERENCES " + TABLE_ROOM + "(id))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table if exists " + TABLE_USERS);
//        db.execSQL("drop table if exists " + TABLE_ROOM);
//        db.execSQL("drop table if exists " + TABLE_APPOINTMENT);
//        db.execSQL("drop table if exists " + TABLE_REPORT);
//        db.execSQL("drop table if exists " + TABLE_REVIEW);
//        db.execSQL("drop table if exists " + TABLE_PHOTO_REVIEW);
//        db.execSQL("drop table if exists " + TABLE_PHOTO_DETAIL_ROOM);
//        db.execSQL("drop table if exists " + TABLE_HISTORY_FIND);
//        db.execSQL("drop table if exists " + TABLE_CONTRACTS);
//        db.execSQL("drop table if exists " + TABLE_FAVORITES);
    }

}
