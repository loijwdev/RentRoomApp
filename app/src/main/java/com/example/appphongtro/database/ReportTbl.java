package com.example.appphongtro.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.appphongtro.model.Report;

public class ReportTbl {
    private DatabaseHelper databaseHelper;
    public ReportTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public boolean saveReportToDatabase(Report report) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("room_id", report.getRoom_id());
        values.put("reporter_id", report.getReport_id());
        values.put("reason", report.getReason());
        long result = db.insert(DatabaseHelper.TABLE_REPORT, null, values);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }
}
