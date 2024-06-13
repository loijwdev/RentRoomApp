package com.example.appphongtro.model;

import java.io.Serializable;
import java.sql.Blob;
import java.util.List;

public class Room implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String address;
    private double are;
    private String amenities;
    private String rules;
    private int owner_id;
    private byte[] image;
    private int status; // 0: da thue , 1: co san

    private double depositMoney;

    private double electricity_money;
    private double water_money;
    private double internet_money;
    private String parking_fee;
    private long timestamp;

    public Room(String name, String description, double price, String address, double are, String amenities, String rules, int owner_id, byte[] image, int status, double depositMoney, double electricity_money, double water_money, double internet_money, String parking_fee) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.address = address;
        this.are = are;
        this.amenities = amenities;
        this.rules = rules;
        this.owner_id = owner_id;
        this.image = image;
        this.status = status;
        this.depositMoney = depositMoney;
        this.electricity_money = electricity_money;
        this.water_money = water_money;
        this.internet_money = internet_money;
        this.parking_fee = parking_fee;
    }

    public Room(int id, String name, double price, String address, byte[] image) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.address = address;
        this.image = image;

    }

    public Room(int id, String name, String description, double price, String address, double are, String amenities, String rules, byte[] image, double depositMoney, double electricity_money, double water_money, double internet_money, String parking_fee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.address = address;
        this.are = are;
        this.amenities = amenities;
        this.rules = rules;
        this.image = image;
        this.depositMoney = depositMoney;
        this.electricity_money = electricity_money;
        this.water_money = water_money;
        this.internet_money = internet_money;
        this.parking_fee = parking_fee;
    }

    public Room() {
    }

    public Room(int id, String name, String description, double price, String address, double are, String amenities, String rules, int owner_id, byte[] image, double depositMoney, double electricity_money, double water_money, double internet_money, String parking_fee) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.address = address;
        this.are = are;
        this.amenities = amenities;
        this.rules = rules;
        this.owner_id = owner_id;
        this.image = image;
        this.depositMoney = depositMoney;
        this.electricity_money = electricity_money;
        this.water_money = water_money;
        this.internet_money = internet_money;
        this.parking_fee = parking_fee;
    }

    public int getId() {
        return id;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getAre() {
        return are;
    }

    public void setAre(double are) {
        this.are = are;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getDepositMoney() {
        return depositMoney;
    }

    public void setDepositMoney(double depositMoney) {
        this.depositMoney = depositMoney;
    }

    public double getElectricity_money() {
        return electricity_money;
    }

    public void setElectricity_money(double electricity_money) {
        this.electricity_money = electricity_money;
    }

    public double getWater_money() {
        return water_money;
    }

    public void setWater_money(double water_money) {
        this.water_money = water_money;
    }

    public double getInternet_money() {
        return internet_money;
    }

    public void setInternet_money(double internet_money) {
        this.internet_money = internet_money;
    }

    public String getParking_fee() {
        return parking_fee;
    }

    public void setParking_fee(String parking_fee) {
        this.parking_fee = parking_fee;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
