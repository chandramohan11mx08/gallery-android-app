package com.gallery.app.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SearchPhotosResponse {

    @SerializedName("photos")
    @Expose
    private Photos photos;
    @SerializedName("stat")
    @Expose
    private String stat;

    public Photos getPhotos() {
        return photos;
    }
}

