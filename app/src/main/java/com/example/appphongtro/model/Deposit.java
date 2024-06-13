package com.example.appphongtro.model;

public class Deposit {
    private int id;
    private int roomId;
    private int tenantId;
    private double amount;
    private String status;
    private String transactionMomoToken;
    private long timestamp;

    public Deposit() {

    }

    public Deposit(int roomId, int tenantId, double amount, String status, String transactionMomoToken, long timestamp) {
        this.roomId = roomId;
        this.tenantId = tenantId;
        this.amount = amount;
        this.status = status;
        this.transactionMomoToken = transactionMomoToken;
        this.timestamp = timestamp;
    }

    // Getter and Setter methods

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getTenantId() {
        return tenantId;
    }

    public void setTenantId(int tenantId) {
        this.tenantId = tenantId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTransactionMomoToken() {
        return transactionMomoToken;
    }

    public void setTransactionMomoToken(String transactionMomoToken) {
        this.transactionMomoToken = transactionMomoToken;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
