package com.example.appphongtro.model;

public class Message {
    String message;
    int senderId;
    long timeStamp;
    private String userName;

    public Message() {
    }

    public Message(String message, int senderId, long timeStamp, String userName) {
        this.message = message;
        this.senderId = senderId;
        this.timeStamp = timeStamp;
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
