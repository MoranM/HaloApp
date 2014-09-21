package com.halo.app.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoryBackground implements Serializable {

    @SerializedName("_id")
    private String id;

    private String imageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundImageUrl() {
        return imageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.imageUrl = backgroundImageUrl;
    }
}
