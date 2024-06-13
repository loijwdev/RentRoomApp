package com.example.appphongtro.model;

import java.io.Serializable;

public class PhotoSlide implements Serializable {
    private int rsourceId;

    public PhotoSlide(int rsourceId) {
        this.rsourceId = rsourceId;
    }

    public int getRsourceId() {
        return rsourceId;
    }

    public void setRsourceId(int rsourceId) {
        this.rsourceId = rsourceId;
    }
}
