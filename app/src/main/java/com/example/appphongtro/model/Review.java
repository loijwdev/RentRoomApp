package com.example.appphongtro.model;

public class Review {
    private int id;
    private int room_id;
    private double rating;
    private String review_text;

    private int tenant_id;

    public Review(int id, int room_id, double rating, String review_text, int tenant_id) {
        this.id = id;
        this.room_id = room_id;
        this.rating = rating;
        this.review_text = review_text;
        this.tenant_id = tenant_id;
    }

    public int getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(int tenant_id) {
        this.tenant_id = tenant_id;
    }


    public Review() {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }
}
