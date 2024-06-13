package com.example.appphongtro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.example.appphongtro.model.Deposit;
import com.example.appphongtro.model.Room;

import java.util.ArrayList;

public class DepositTbl {
    private DatabaseHelper databaseHelper;

    public DepositTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long addDepositToDatabase(Deposit deposit) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("room_id", deposit.getRoomId());
        values.put("tenant_id", deposit.getTenantId());
        values.put("amount", deposit.getAmount());
        values.put("status", deposit.getStatus());
        values.put("transactionMomoToken", deposit.getTransactionMomoToken());
        values.put("timestamp", System.currentTimeMillis()); // Lưu thời điểm đặt cọc

        long result = db.insert(DatabaseHelper.TABLE_DEPOSIT, null, values);

        if (result == -1) {
            Log.e("Database Error", "Failed to insert deposit into database");
        } else {
            Log.d("Database Success", "Deposit inserted successfully with ID: " + result);
        }

        return result;
    }

    public long addOrUpdateDeposit(Deposit deposit) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // Kiểm tra xem giao dịch đã tồn tại hay chưa bằng roomId và tenantId
        boolean isTransactionExists = checkTransactionExists(deposit.getRoomId(), deposit.getTenantId());

        ContentValues values = new ContentValues();
        values.put("room_id", deposit.getRoomId());
        values.put("tenant_id", deposit.getTenantId());
        values.put("amount", deposit.getAmount());
        values.put("status", deposit.getStatus());
        values.put("transactionMomoToken", deposit.getTransactionMomoToken());
        values.put("timestamp", System.currentTimeMillis());

        long result;

        if (isTransactionExists) {
            // Nếu đã tồn tại, cập nhật thông tin
            result = db.update(DatabaseHelper.TABLE_DEPOSIT, values, "room_id=? AND tenant_id=?", // Thêm điều kiện kiểm tra giao dịch
                    new String[]{String.valueOf(deposit.getRoomId()), String.valueOf(deposit.getTenantId())});
        } else {
            // Nếu chưa tồn tại, thêm mới
            result = db.insert(DatabaseHelper.TABLE_DEPOSIT, null, values);
        }

        return result;
    }


    private boolean checkTransactionExists(int roomId, int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_DEPOSIT, new String[]{"room_id", "tenant_id"}, "room_id=? AND tenant_id=?", new String[]{String.valueOf(roomId), String.valueOf(tenantId)}, null, null, null);

        boolean exists = cursor.moveToFirst();

        cursor.close();

        return exists;
    }

    public String getStatus(int roomId, int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Cột cần lấy giá trị trạng thái
        String[] projection = {"status"};

        // Điều kiện WHERE
        String selection = "room_id = ? AND tenant_id = ?";
        String[] selectionArgs = {String.valueOf(roomId), String.valueOf(tenantId)};

        // Thực hiện truy vấn
        Cursor cursor = db.query(DatabaseHelper.TABLE_DEPOSIT, projection, selection, selectionArgs, null, null, null);

        String status = null;

        // Kiểm tra xem có dữ liệu không
        if (cursor != null && cursor.moveToFirst()) {
            // Lấy giá trị từ cột "status"
            status = cursor.getString(cursor.getColumnIndexOrThrow("status"));
            cursor.close();
        }
        return status;
    }

    public Cursor getRoomInfoForTenant(int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = " SELECT " +
                DatabaseHelper.TABLE_ROOM + ".image AS imgRoom, " +
                DatabaseHelper.TABLE_ROOM + ".name AS roomName, " +
                DatabaseHelper.TABLE_ROOM + ".price AS roomPrice, " +
                DatabaseHelper.TABLE_ROOM + ".address AS roomAdress, " +
                DatabaseHelper.TABLE_ROOM + ".id AS roomId, " +
                DatabaseHelper.TABLE_DEPOSIT + ".timestamp AS timestamp, " +
                DatabaseHelper.TABLE_DEPOSIT + ".amount AS amount, " +
                DatabaseHelper.TABLE_DEPOSIT + ".status AS status " +
                "FROM " + DatabaseHelper.TABLE_DEPOSIT +
                " JOIN " + DatabaseHelper.TABLE_ROOM + " ON " + DatabaseHelper.TABLE_DEPOSIT + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id " +
                "WHERE " + DatabaseHelper.TABLE_DEPOSIT + ".tenant_id = " + tenantId +
                " AND " + DatabaseHelper.TABLE_DEPOSIT + ".status != 'Chưa thanh toán'";

        System.out.println(query);
        Cursor cursor = db.rawQuery(query, null);

        // Trả về con trỏ
        return cursor;
    }

    public Cursor getRoomsDeposited(int owner_id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = " SELECT " +
                DatabaseHelper.TABLE_ROOM + ".image AS imgRoom, " +
                DatabaseHelper.TABLE_ROOM + ".name AS roomName, " +
                DatabaseHelper.TABLE_ROOM + ".price AS roomPrice, " +
                DatabaseHelper.TABLE_ROOM + ".address AS roomAdress, " +
                DatabaseHelper.TABLE_DEPOSIT + ".timestamp AS timestamp, " +
                DatabaseHelper.TABLE_DEPOSIT + ".amount AS amount, " +
                DatabaseHelper.TABLE_DEPOSIT + ".room_id AS IdRoom, " +
                DatabaseHelper.TABLE_DEPOSIT + ".tenant_id AS IdTenant, " +
                DatabaseHelper.TABLE_DEPOSIT + ".status AS status, " +
                DatabaseHelper.TABLE_USERS + ".fullName as Name, " +
                DatabaseHelper.TABLE_USERS + ".phoneNumber as phoneNumber " +
                "FROM " + DatabaseHelper.TABLE_DEPOSIT +
                " JOIN " + DatabaseHelper.TABLE_ROOM + " ON " + DatabaseHelper.TABLE_DEPOSIT + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id " +
                " JOIN " + DatabaseHelper.TABLE_USERS + " ON " + DatabaseHelper.TABLE_DEPOSIT + ".tenant_id = " + DatabaseHelper.TABLE_USERS + ".id " +
                "WHERE " + DatabaseHelper.TABLE_ROOM + ".owner_id = " + owner_id +
                " AND " + DatabaseHelper.TABLE_ROOM + ".status = 0";
        System.out.println(query);
        Cursor cursor = db.rawQuery(query, null);
        // Trả về con trỏ
        return cursor;

    }




}
