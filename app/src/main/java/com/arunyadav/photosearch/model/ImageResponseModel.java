package com.arunyadav.photosearch.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageResponseModel {

    @SerializedName("photos")
    @Expose
    private photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @SerializedName("message")
    @Expose
    private String message;


    public photos getPhotos() {
        return photos;
    }

    public void setPhotos(photos photos) {
        this.photos = photos;
    }

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }
}
