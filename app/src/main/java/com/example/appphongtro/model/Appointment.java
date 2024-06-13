package com.example.appphongtro.model;

import java.util.Date;

public class Appointment {

    private int id;
    private int tenant_id;
    private int room_id;
    private Date appointmentDate;
    private String note;
    private String status;
    private int hasVisited;

    public Appointment() {
    }

    public Appointment(int id, int tenant_id, int room_id, Date appointmentDate, String note, String status, int hasVisited) {
        this.id = id;
        this.tenant_id = tenant_id;
        this.room_id = room_id;
        this.appointmentDate = appointmentDate;
        this.note = note;
        this.status = status;
        this.hasVisited = hasVisited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(int tenant_id) {
        this.tenant_id = tenant_id;
    }

    public int getRoom_id() {
        return room_id;
    }

    public void setRoom_id(int room_id) {
        this.room_id = room_id;
    }

    public Date getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Date appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHasVisited() {
        return hasVisited;
    }

    public void setHasVisited(int hasVisited) {
        this.hasVisited = hasVisited;
    }
}
