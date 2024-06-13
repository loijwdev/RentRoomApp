package com.example.appphongtro.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.example.appphongtro.model.Room;

import java.util.ArrayList;

public class RoomTbl {
    private DatabaseHelper databaseHelper;

    public RoomTbl(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public long addRoom(Room room) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", room.getName());
        cv.put("description", room.getDescription());
        cv.put("price", room.getPrice());
        cv.put("address", room.getAddress());
        cv.put("area", room.getAre());
        cv.put("amenities", room.getAmenities());
        cv.put("rules", room.getRules());
        cv.put("owner_id", room.getOwner_id());
        cv.put("image", room.getImage());
        cv.put("status", room.getStatus());
        cv.put("deposit_money", room.getDepositMoney());
        cv.put("timestamp", System.currentTimeMillis());
        cv.put("electricity_money", room.getElectricity_money());
        cv.put("water_money", room.getWater_money());
        cv.put("internet_money", room.getInternet_money());
        cv.put("parking_fee", room.getParking_fee());
        long id = db.insertOrThrow(DatabaseHelper.TABLE_ROOM, null, cv);
        return id;
    }

    public ArrayList<Room> getAllRoom() {
        ArrayList<Room> roomModels = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_ROOM + " WHERE status != 0", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            double price = (cursor.getDouble(3));
            String address = cursor.getString(4);
            byte[] imgRoom = cursor.getBlob(9);
            Room model = new Room(id, name, price, address, imgRoom);
            roomModels.add(model);
        }
        cursor.close();
        return roomModels;
    }
    public ArrayList<Room> getAllRoomAdmin() {
        ArrayList<Room> roomModels = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from " + DatabaseHelper.TABLE_ROOM, null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String description = cursor.getString(2);
            double area = cursor.getDouble(5);
            double price = (cursor.getDouble(3));
            String amenities = cursor.getString(6);
            String rule = cursor.getString(7);

            String address = cursor.getString(4);
            byte[] imgRoom = cursor.getBlob(9);
            double deposit_money = cursor.getDouble(11);
            double electricity_money = cursor.getDouble(13);
            double water_money = cursor.getDouble(14);
            double internet_money = cursor.getDouble(15);
            String parking_fee = cursor.getString(16);
            Room model = new Room( id,  name,  description,  price,  address,  area,  amenities,  rule,imgRoom,  deposit_money,  electricity_money,  water_money,  internet_money,  parking_fee);
            roomModels.add(model);
        }
        cursor.close();
        return roomModels;
    }

    public Boolean editRoom(Room room) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", room.getName());

        cv.put("description", room.getDescription());
        cv.put("price", room.getPrice());
        cv.put("address", room.getAddress());
        cv.put("area", room.getAre());
        cv.put("amenities", room.getAmenities());
        cv.put("rules", room.getRules());
        cv.put("image", room.getImage());
        cv.put("deposit_money", room.getDepositMoney());
        cv.put("timestamp", System.currentTimeMillis());
        cv.put("electricity_money", room.getElectricity_money());
        cv.put("water_money", room.getWater_money());
        cv.put("internet_money", room.getInternet_money());
        cv.put("parking_fee", room.getParking_fee());
        long result = db.update(DatabaseHelper.TABLE_ROOM, cv, "id = ?", new String[]{String.valueOf(room.getId())});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean deleteRoom(int id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        long result = db.delete(DatabaseHelper.TABLE_ROOM, "id = ?", new String[]{String.valueOf(id)});
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("Range")
    public ArrayList<Room> searchRooms(String location, String area, String benefit, String priceRange) {
        ArrayList<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_ROOM + " WHERE 1=1"; // 1=1 để giữ cho câu truy vấn linh hoạt

        if (!TextUtils.isEmpty(location)) {
            query += " AND address LIKE '%" + location + "%'";
        }

        if (!TextUtils.isEmpty(area)) {
            query += " AND area >= " + area;
        }

        if (!TextUtils.isEmpty(benefit)) {
            query += " AND amenities LIKE '%" + benefit + "%'";
        }

        if (!TextUtils.isEmpty(priceRange)) {
            // Check if priceRange is in the format "X$ +"
            if (priceRange.endsWith("$ +")) {
                // Extract the minimum price (X)
                int minPrice = Integer.parseInt(priceRange.replace("$ +", "").trim());
                query += " AND price >= " + minPrice;
            } else {
                // Otherwise, process the price range normally
                String[] priceRangeArray = priceRange.split(" - ");
                if (priceRangeArray.length == 2) {
                    int minPrice = Integer.parseInt(priceRangeArray[0].replace("$", "").trim());
                    int maxPrice = Integer.parseInt(priceRangeArray[1].replace("$", "").trim());
                    query += " AND price BETWEEN " + minPrice + " AND " + maxPrice;
                }
            }
        }

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                // Đọc dữ liệu từ cursor và đổ vào đối tượng Room
                room.setId(cursor.getInt(cursor.getColumnIndex("id")));
                room.setName(cursor.getString(cursor.getColumnIndex("name")));
                room.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                room.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
                room.setAmenities(cursor.getString(cursor.getColumnIndex("amenities")));
                room.setAre(cursor.getDouble(cursor.getColumnIndex("area")));
                room.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                room.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roomList;
    }

    @SuppressLint("Range")
    public Room getRoomById(int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Room room = null;

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_ROOM + " WHERE id = " + roomId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            room = new Room();
            room.setId(cursor.getInt(cursor.getColumnIndex("id")));
            room.setName(cursor.getString(cursor.getColumnIndex("name")));
            room.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            room.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
            room.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            room.setAre(cursor.getDouble(cursor.getColumnIndex("area")));
            room.setAmenities(cursor.getString(cursor.getColumnIndex("amenities")));
            room.setRules(cursor.getString(cursor.getColumnIndex("rules")));
            room.setOwner_id(cursor.getInt(cursor.getColumnIndex("owner_id")));
            room.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
            room.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            room.setDepositMoney(cursor.getDouble(cursor.getColumnIndex("deposit_money")));
            room.setElectricity_money(cursor.getDouble(cursor.getColumnIndex("electricity_money")));
            room.setWater_money(cursor.getDouble(cursor.getColumnIndex("water_money")));
            room.setInternet_money(cursor.getDouble(cursor.getColumnIndex("internet_money")));
            room.setParking_fee(cursor.getString(cursor.getColumnIndex("parking_fee")));
            room.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
        }

        cursor.close();
        db.close();
        return room;
    }

    @SuppressLint("Range")
    public int getOwnerIdByRoomId(int roomId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        int ownerId = -1; // Default value indicating failure

        String query = "SELECT owner_id FROM " + DatabaseHelper.TABLE_ROOM + " WHERE id = " + roomId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            ownerId = cursor.getInt(cursor.getColumnIndex("owner_id"));
        }

        cursor.close();
        db.close();
        return ownerId;
    }

    @SuppressLint("Range")
    public ArrayList<Room> getRoomsByOwnerId(int ownerId) {
        ArrayList<Room> roomList = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();

        String query = "SELECT * FROM " + DatabaseHelper.TABLE_ROOM + " WHERE owner_id = " + ownerId;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                Room room = new Room();
                // Read data from cursor and populate the Room object
                room.setId(cursor.getInt(cursor.getColumnIndex("id")));
                room.setName(cursor.getString(cursor.getColumnIndex("name")));
                room.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                room.setImage(cursor.getBlob(cursor.getColumnIndex("image")));
                room.setAmenities(cursor.getString(cursor.getColumnIndex("amenities")));
                room.setAre(cursor.getDouble(cursor.getColumnIndex("area")));
                room.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                room.setPrice(cursor.getDouble(cursor.getColumnIndex("price")));
                room.setRules(cursor.getString(cursor.getColumnIndex("rules")));
                room.setDepositMoney(cursor.getDouble(cursor.getColumnIndex("deposit_money")));
                room.setOwner_id(cursor.getInt(cursor.getColumnIndex("owner_id")));
                room.setElectricity_money(cursor.getColumnIndex("electricity_money"));
                room.setWater_money(cursor.getDouble(cursor.getColumnIndex("water_money")));
                room.setInternet_money(cursor.getDouble(cursor.getColumnIndex("internet_money")));
                room.setParking_fee(cursor.getString(cursor.getColumnIndex("parking_fee")));
                roomList.add(room);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return roomList;
    }

    public boolean updateRoomStatus(int roomId, int newStatus) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("status", newStatus);

        int rowsAffected = db.update(DatabaseHelper.TABLE_ROOM, cv, "id = ?", new String[]{String.valueOf(roomId)});
        db.close();

        // If at least one row is affected, return true, indicating success
        return rowsAffected > 0;
    }
    @SuppressLint("Range")
    public String getOwnerNameByRoomId(int room_id) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        String ownerName = null;

        // Assuming owner_id is a foreign key in the TABLE_ROOM table
        String query = "SELECT " + DatabaseHelper.TABLE_USERS + ".fullName " +
                "FROM " + DatabaseHelper.TABLE_ROOM +
                " INNER JOIN " + DatabaseHelper.TABLE_USERS +
                " ON " + DatabaseHelper.TABLE_ROOM + ".owner_id = " + DatabaseHelper.TABLE_USERS + ".id " +
                "WHERE " + DatabaseHelper.TABLE_ROOM + ".id = " + room_id;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            ownerName = cursor.getString(cursor.getColumnIndex("fullName"));
        }

        cursor.close();
        db.close();
        return ownerName;
    }



}
