package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.appphongtro.model.Appointment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AppointmentDb {
    private DatabaseHelper databaseHelper;

    public AppointmentDb(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public Boolean addAppointment(Appointment appointment) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("tenant_id", appointment.getTenant_id());
        cv.put("room_id", appointment.getRoom_id());
        cv.put("appointmentDate", appointment.getAppointmentDate().getTime()); // Chuyển đổi Date thành timestamp
        cv.put("note", appointment.getNote());
        cv.put("status", appointment.getStatus());
        cv.put("hasVisited", appointment.getHasVisited());
        long result = db.insert(DatabaseHelper.TABLE_APPOINTMENT, null, cv);
        if(result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAppointmentDetailsForOwner(int ownerId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Lấy thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();
        String currentDateTime = String.valueOf(currentTimeMillis);

        String query = "SELECT " +
                DatabaseHelper.TABLE_ROOM + ".image AS imgRoom, " +
                DatabaseHelper.TABLE_USERS + ".fullName AS tenantName, " +
                DatabaseHelper.TABLE_USERS + ".phoneNumber AS phoneNumber, " +
                DatabaseHelper.TABLE_ROOM + ".name AS roomName, " +
                DatabaseHelper.TABLE_APPOINTMENT + ".id AS appointmentId, " +
                DatabaseHelper.TABLE_APPOINTMENT + ".hasVisited AS hasVisited, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate AS appointmentDateTime, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".status AS statusBooking " +
                "FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " JOIN " + DatabaseHelper.TABLE_ROOM + " ON " + DatabaseHelper.TABLE_APPOINTMENT + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id " +
                " JOIN " + DatabaseHelper.TABLE_USERS + " ON " + DatabaseHelper.TABLE_APPOINTMENT + ".tenant_id = " + DatabaseHelper.TABLE_USERS + ".id " +
                "WHERE " + DatabaseHelper.TABLE_ROOM + ".owner_id = ?" +
                " AND " + DatabaseHelper.TABLE_APPOINTMENT + ".status != ?" +
                " ORDER BY " + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate ASC";
        System.out.println(query);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ownerId), "Không thành công"});


        // Chuyển con trỏ đến vị trí đầu tiên
        cursor.moveToFirst();

        // Trả về con trỏ
        return cursor;
    }

    public Cursor getAppointmentDetailsForTenant(int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        // Lấy thời gian hiện tại
        long currentTimeMillis = System.currentTimeMillis();
        String currentDateTime = String.valueOf(currentTimeMillis);
        String query = " SELECT " +
                DatabaseHelper.TABLE_ROOM + ".image AS imgRoom, " +
                DatabaseHelper.TABLE_ROOM + ".name AS roomName, " +
                DatabaseHelper.TABLE_ROOM + ".id AS roomId, " +
                DatabaseHelper.TABLE_APPOINTMENT + ".id AS appointmentId, " +
                DatabaseHelper.TABLE_APPOINTMENT + ".hasVisited AS hasVisited, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate AS appointmentDateTime, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".status AS statusBooking " +
                "FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " JOIN " + DatabaseHelper.TABLE_ROOM + " ON " + DatabaseHelper.TABLE_APPOINTMENT + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id " +
                "WHERE " + DatabaseHelper.TABLE_APPOINTMENT + ".tenant_id = " + tenantId +
//                " AND " + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate >= " + currentDateTime +
                " ORDER BY " + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate ASC";

        System.out.println(query);

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, null);

        // Trả về con trỏ
        return cursor;
    }
    @SuppressLint("Range")
    public List<String> getBookedTimeSlots(int roomId, Date startDate, int currentTenantId) {
        List<String> bookedTimeSlots = new ArrayList<>();

        // Chuyển đổi thời gian thành timestamp
        long startTimeStamp = startDate.getTime();

        // Tính toán thời gian kết thúc bằng cách cộng thêm 2 giờ
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.HOUR, 2);
        long endTimeStamp = calendar.getTimeInMillis();

        // Lấy ngày bắt đầu của startDate (không tính giờ, phút, giây)
        Calendar startOfDay = Calendar.getInstance();
        startOfDay.setTime(startDate);
        startOfDay.set(Calendar.HOUR_OF_DAY, 0);
        startOfDay.set(Calendar.MINUTE, 0);
        startOfDay.set(Calendar.SECOND, 0);
        startOfDay.set(Calendar.MILLISECOND, 0);

        // Lấy ngày kết thúc của startDate (không tính giờ, phút, giây)
        Calendar endOfDay = Calendar.getInstance();
        endOfDay.setTime(startDate);
        endOfDay.set(Calendar.HOUR_OF_DAY, 23);
        endOfDay.set(Calendar.MINUTE, 59);
        endOfDay.set(Calendar.SECOND, 59);
        endOfDay.set(Calendar.MILLISECOND, 999);

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String query = "SELECT appointmentDate FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " WHERE room_id = ? AND tenant_id != ? AND appointmentDate BETWEEN ? AND ?";

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(roomId), String.valueOf(currentTenantId), String.valueOf(startOfDay.getTimeInMillis()), String.valueOf(endOfDay.getTimeInMillis())});

        // Kiểm tra và xử lý kết quả truy vấn
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    // Lấy thời điểm từ cột "appointmentDate"
                    long appointmentTimeStamp = cursor.getLong(cursor.getColumnIndex("appointmentDate"));

                    // Chuyển đổi timestamp thành đối tượng Date
                    Date appointmentDate = new Date(appointmentTimeStamp);

                    // Format thời điểm thành chuỗi "HH:mm"
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
                    String timeSlot = sdf.format(appointmentDate);

                    // Thêm vào danh sách
                    bookedTimeSlots.add(timeSlot);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        db.close();

        return bookedTimeSlots;
    }


    @SuppressLint("Range")
    public Appointment getAppointment(int roomId, int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        // Câu truy vấn SQL để lấy thông tin lịch hẹn
        String query = "SELECT * FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " WHERE room_id = ? AND tenant_id = ? ";

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(roomId), String.valueOf(tenantId)});

        // Kiểm tra và xử lý kết quả truy vấn
        Appointment appointment = null;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                appointment = new Appointment();
                // Đọc thông tin lịch hẹn từ Cursor và set cho đối tượng Appointment
                appointment.setId(cursor.getInt(cursor.getColumnIndex("id")));
                appointment.setRoom_id(cursor.getInt(cursor.getColumnIndex("room_id")));
                appointment.setTenant_id(cursor.getInt(cursor.getColumnIndex("tenant_id")));
                appointment.setAppointmentDate(new Date(cursor.getLong(cursor.getColumnIndex("appointmentDate"))));
                appointment.setNote(cursor.getString(cursor.getColumnIndex("note")));
                appointment.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            }
            cursor.close();
        }
        db.close();

        return appointment;
    }

    public boolean updateAppointment(Appointment appointment) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        // Set thông tin mới cho lịch hẹn
        values.put("note", appointment.getNote());
        values.put("status", appointment.getStatus());
        values.put("appointmentDate", appointment.getAppointmentDate().getTime()); // Set giá trị mới cho appointmentDate

        // Câu điều kiện để xác định lịch hẹn cần cập nhật
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(appointment.getId())};

        // Thực hiện cập nhật
        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values, whereClause, whereArgs);

        // Đóng kết nối database
        db.close();

        // Kiểm tra xem có dòng nào được cập nhật không
        return rowsAffected > 0;
    }

    public void  updateStatusAppointment(int appointmentId, String status) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("status", status);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(appointmentId)};

        // Thực hiện cập nhật
        int rowsAffected = db.update(DatabaseHelper.TABLE_APPOINTMENT, values, whereClause, whereArgs);
        db.close();
    }
    @SuppressLint("Range")
    public boolean isAppointmentSuccessful(int appointmentId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        boolean isSuccessful = false;

        String query = "SELECT status FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " WHERE id = ?";

        // Thực hiện truy vấn
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(appointmentId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                String status = cursor.getString(cursor.getColumnIndex("status"));
                // Kiểm tra xem trạng thái có phải "Thành công" hay không
                isSuccessful = "Thành công".equals(status);
            }
            cursor.close();
        }
        db.close();

        return isSuccessful;
    }

    public void deleteAppointment(int appointmentId, int roomId) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String whereClause = "id = ? and room_id =?"; //
        String[] whereArgs = {String.valueOf(appointmentId), String.valueOf(roomId)};

        // Thực hiện truy vấn xóa
        db.delete(DatabaseHelper.TABLE_APPOINTMENT, whereClause, whereArgs);
        // Đóng kết nối database
        db.close();
    }

    public Cursor queryAppointments(int tenantId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        // Your actual query to fetch appointments
        String query = "SELECT " +
                DatabaseHelper.TABLE_ROOM + ".image AS imgRoom, " +
                DatabaseHelper.TABLE_ROOM + ".name AS roomName, " +
                DatabaseHelper.TABLE_ROOM + ".id AS roomId, " +
                DatabaseHelper.TABLE_APPOINTMENT + ".id AS appointmentId, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate AS appointmentDateTime, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".status AS statusBooking " +
                "FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " JOIN " + DatabaseHelper.TABLE_ROOM + " ON " + DatabaseHelper.TABLE_APPOINTMENT + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id " +
                "WHERE " + DatabaseHelper.TABLE_APPOINTMENT + ".tenant_id = " + tenantId ;


        Cursor cursor = db.rawQuery(query,null);


        // Chuyển con trỏ đến vị trí đầu tiên
        cursor.moveToFirst();

        // Trả về con trỏ
        return cursor;
    }

    public Cursor queryAppointmentsForOwner(int ownerId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        // Your actual query to fetch appointments
        String query = "SELECT " +
                DatabaseHelper.TABLE_ROOM + ".image AS imgRoom, " +
                DatabaseHelper.TABLE_USERS + ".fullName AS tenantName, " +
                DatabaseHelper.TABLE_USERS + ".phoneNumber AS phoneNumber, " +
                DatabaseHelper.TABLE_ROOM + ".name AS roomName, " +
                DatabaseHelper.TABLE_APPOINTMENT + ".id AS appointmentId, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate AS appointmentDateTime, " +
                "" + DatabaseHelper.TABLE_APPOINTMENT + ".status AS statusBooking " +
                "FROM " + DatabaseHelper.TABLE_APPOINTMENT +
                " JOIN " + DatabaseHelper.TABLE_ROOM + " ON " + DatabaseHelper.TABLE_APPOINTMENT + ".room_id = " + DatabaseHelper.TABLE_ROOM + ".id " +
                " JOIN " + DatabaseHelper.TABLE_USERS + " ON " + DatabaseHelper.TABLE_APPOINTMENT + ".tenant_id = " + DatabaseHelper.TABLE_USERS + ".id " +
                "WHERE " + DatabaseHelper.TABLE_ROOM + ".owner_id = ?" +
                " AND " + DatabaseHelper.TABLE_APPOINTMENT + ".status != ?" +
                " ORDER BY " + DatabaseHelper.TABLE_APPOINTMENT + ".appointmentDate ASC";


        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(ownerId), "Không thành công"});


        // Chuyển con trỏ đến vị trí đầu tiên
        cursor.moveToFirst();

        // Trả về con trỏ
        return cursor;

    }

    public void updateVisitedStatus(int appointmentId, int hasVisited) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("hasVisited", hasVisited);
        String whereClause = "id = ?";
        String[] whereArgs = {String.valueOf(appointmentId)};

        // Thực hiện cập nhật
        db.update(DatabaseHelper.TABLE_APPOINTMENT, values, whereClause, whereArgs);
        db.close();
    }

}
