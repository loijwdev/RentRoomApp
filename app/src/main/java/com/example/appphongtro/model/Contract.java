package com.example.appphongtro.model;
import java.util.Date;
public class Contract {
    private int id;
    private int tenant_id;
    private int room_id;
    private Date start_date;
    private Date end_date;
    private Double deposit;
    private Double rent;
    private Double other_fees;
    private String status;
    private String tenant_name;
    private byte[] image;

    private String isPaidRent;


    public Contract() {
    }

    public Contract(int tenant_id, int room_id, Date start_date, Date end_date, Double deposit, Double rent, Double other_fees, String status, String isPaidRent) {
        this.tenant_id = tenant_id;
        this.room_id = room_id;
        this.start_date = start_date;
        this.end_date = end_date;
        this.deposit = deposit;
        this.rent = rent;
        this.other_fees = other_fees;
        this.status = status;
        this.isPaidRent = isPaidRent;
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

    public Date getStart_date() {
        return start_date;
    }

    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    public Date getEnd_date() {
        return end_date;
    }

    public void setEnd_date(Date end_date) {
        this.end_date = end_date;
    }

    public Double getDeposit() {
        return deposit;
    }

    public void setDeposit(Double deposit) {
        this.deposit = deposit;
    }

    public Double getRent() {
        return rent;
    }

    public void setRent(Double rent) {
        this.rent = rent;
    }

    public Double getOther_fees() {
        return other_fees;
    }

    public void setOther_fees(Double other_fees) {
        this.other_fees = other_fees;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTenant_name() {
        return tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        this.tenant_name = tenant_name;
    }
    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getIsPaidRent() {
        return isPaidRent;
    }

    public void setIsPaidRent(String isPaidRent) {
        this.isPaidRent = isPaidRent;
    }
}
