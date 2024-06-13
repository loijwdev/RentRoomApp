package com.example.appphongtro.model;

import android.net.Uri;

public class PhotoReview {

    private int id;
    private int review_id;
    private Uri imageUri;

    public PhotoReview(int id, int review_id, Uri imageUri) {
        this.id = id;
        this.review_id = review_id;
        this.imageUri = imageUri;
    }

    public PhotoReview() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getReview_id() {
        return review_id;
    }

    public void setReview_id(int review_id) {
        this.review_id = review_id;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(Uri imageUri) {
        this.imageUri = imageUri;
    }
}
