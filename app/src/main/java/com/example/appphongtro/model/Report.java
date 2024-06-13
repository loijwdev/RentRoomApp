package com.example.appphongtro.model;

public class Report {

    private int id;
    private int room_id;
    private int report_id;
    private String reason;

    public Report(int id, int room_id, int report_id, String reason) {
        this.id = id;
        this.room_id = room_id;
        this.report_id = report_id;
        this.reason = reason;
    }

    public Report() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
