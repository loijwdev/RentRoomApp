package com.example.appphongtro.model;

import android.net.Uri;

public class PhotoRoom {
    private int id;
    private int room_id;
    private Uri imageRoomUri;

    public PhotoRoom(int id, int room_id, Uri imageRoomUri) {
        this.id = id;
        this.room_id = room_id;
        this.imageRoomUri = imageRoomUri;
    }

    public PhotoRoom() {
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

    public Uri getImageRoomUri() {
        return imageRoomUri;
    }

    public void setImageRoomUri(Uri imageRoomUri) {
        this.imageRoomUri = imageRoomUri;
    }
}
