package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appphongtro.model.Contract;

import java.util.Date;

public class ContractTbl {

    private DatabaseHelper databaseHelper;

    public ContractTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public Boolean addContract(Contract contract) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("tenant_id", contract.getTenant_id());
        cv.put("room_id", contract.getRoom_id());
        cv.put("start_date", contract.getStart_date().getTime());

        // Check if end_date is not null before accessing getTime()
        if (contract.getEnd_date() != null) {
            cv.put("end_date", contract.getEnd_date().getTime());
        } else {
            cv.putNull("end_date");
        }

        cv.put("deposit", contract.getDeposit());
        cv.put("rent", contract.getRent());
        cv.put("other_fees", contract.getOther_fees());
        cv.put("status", contract.getStatus());
        cv.put("isPaidRent", contract.getIsPaidRent());

        long result = db.insert(DatabaseHelper.TABLE_CONTRACTS, null, cv);
        return result != -1;
    }

    public boolean isContractCreated(int tenantId, int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_CONTRACTS +
                " WHERE tenant_id = " + tenantId +
                " AND room_id = " + roomId;

        Cursor cursor = db.rawQuery(query, null);

        boolean contractExists = cursor.moveToFirst();

        cursor.close();
        db.close();

        return contractExists;
    }

    @SuppressLint("Range")
    public Contract getContractDetails(int tenantId, int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT "
                + DatabaseHelper.TABLE_CONTRACTS + ".tenant_id, "
                + DatabaseHelper.TABLE_CONTRACTS + ".room_id, "
                + DatabaseHelper.TABLE_CONTRACTS + ".start_date, "
                + DatabaseHelper.TABLE_CONTRACTS + ".end_date, "
                + DatabaseHelper.TABLE_CONTRACTS + ".deposit, "
                + DatabaseHelper.TABLE_CONTRACTS + ".rent, "
                + DatabaseHelper.TABLE_CONTRACTS + ".other_fees, "
                + DatabaseHelper.TABLE_CONTRACTS + ".status, "
                + DatabaseHelper.TABLE_USERS + ".fullName AS tenant_name, "
                + DatabaseHelper.TABLE_ROOM + ".image AS room_image "
                + "FROM " + DatabaseHelper.TABLE_CONTRACTS
                + " INNER JOIN " + DatabaseHelper.TABLE_USERS
                + " ON " + DatabaseHelper.TABLE_CONTRACTS + ".tenant_id = " + DatabaseHelper.TABLE_USERS + ".id"
                + " INNER JOIN " + DatabaseHelper.TABLE_ROOM
                + " ON " + DatabaseHelper.TABLE_CONTRACTS + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id"
                + " WHERE " + DatabaseHelper.TABLE_CONTRACTS + ".tenant_id = " + tenantId
                + " AND " + DatabaseHelper.TABLE_CONTRACTS + ".room_id = " + roomId;

        Cursor cursor = db.rawQuery(query, null);

        Contract contract = null;

        if (cursor.moveToFirst()) {
            contract = new Contract();
            contract.setTenant_id(cursor.getInt(cursor.getColumnIndex("tenant_id")));
            contract.setRoom_id(cursor.getInt(cursor.getColumnIndex("room_id")));
            contract.setStart_date(new Date(cursor.getLong(cursor.getColumnIndex("start_date"))));
            // Check if end_date is not null before accessing cursor.getLong
            if (!cursor.isNull(cursor.getColumnIndex("end_date"))) {
                contract.setEnd_date(new Date(cursor.getLong(cursor.getColumnIndex("end_date"))));
            }
            contract.setDeposit(cursor.getDouble(cursor.getColumnIndex("deposit")));
            contract.setRent(cursor.getDouble(cursor.getColumnIndex("rent")));
            contract.setOther_fees(cursor.getDouble(cursor.getColumnIndex("other_fees")));
            contract.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            contract.setTenant_name(cursor.getString(cursor.getColumnIndex("tenant_name")));
            contract.setImage(cursor.getBlob(cursor.getColumnIndex("room_image")));
        }

        cursor.close();
        db.close();

        return contract;
    }

    @SuppressLint("Range")
    public String getContractStatus (int tenantId, int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String status = null;

        String query = "SELECT status FROM " + DatabaseHelper.TABLE_CONTRACTS +
                " WHERE tenant_id = " + tenantId +
                " AND room_id = " + roomId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            status = cursor.getString(cursor.getColumnIndex("status"));
        }

        cursor.close();
        db.close();

        return status;
    }

    @SuppressLint("Range")
    public Date getContractStartDate(int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Date startDate = null;

        String query = "SELECT start_date FROM " + DatabaseHelper.TABLE_CONTRACTS +
                " WHERE tenant_id = " + tenantId
                + " ORDER BY start_date ASC";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            long startDateMillis = cursor.getLong(cursor.getColumnIndex("start_date"));
            startDate = new Date(startDateMillis);
        }

        cursor.close();
        db.close();

        return startDate;
    }

    @SuppressLint("Range")
    public void updateIsPaidRent(int tenantId, int roomId, String isPaid) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("isPaidRent", isPaid);

        String whereClause = "tenant_id = ? AND room_id = ?";
        String[] whereArgs = {String.valueOf(tenantId), String.valueOf(roomId)};

        db.update(DatabaseHelper.TABLE_CONTRACTS, values, whereClause, whereArgs);

        db.close();
    }

    @SuppressLint("Range")
    public String isRentPaid(int tenantId, int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String isPaid = null;

        String query = "SELECT isPaidRent FROM " + DatabaseHelper.TABLE_CONTRACTS +
                " WHERE tenant_id = " + tenantId +
                " AND room_id = " + roomId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            isPaid = cursor.getString(cursor.getColumnIndex("isPaidRent"));
        }

        cursor.close();
        db.close();
        return isPaid;
    }

    @SuppressLint("Range")
    public String isRentPaid1(int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String isPaid = null;

        String query = "SELECT isPaidRent FROM " + DatabaseHelper.TABLE_CONTRACTS +
                " WHERE tenant_id = " + tenantId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            isPaid = cursor.getString(cursor.getColumnIndex("isPaidRent"));
        }

        cursor.close();
        db.close();
        return isPaid;
    }
}

