package com.example.appphongtro.model;

public class User {

    private int id;
    private String email;
    private String username;
    private String fullName;
    private String phoneNumber;
    private String password;
    private String address;
    private int role; // 0:admin, 1:chunha, 2:ng thue

    private byte[] avatar;

    private int isLocked; // 0:bth 1:lock

    public User() {
    }

    public User(int id, String email, String fullName, String phoneNumber, String password, String address, byte[] avatar) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.avatar = avatar;
    }

    public User(String email, String username, String fullName, String phoneNumber, String password, String address, int role, int isLocked) {
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.role = role;
        this.isLocked = isLocked;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public int getIsLocked() {
        return isLocked;
    }

    public void setIsLocked(int isLocked) {
        this.isLocked = isLocked;
    }
}
