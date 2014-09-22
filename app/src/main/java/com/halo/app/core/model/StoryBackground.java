package com.halo.app.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoryBackground implements Serializable {

    @SerializedName("_id")
    private String id;

    private String backgroundImageUrl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBackgroundImageUrl() {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl) {
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
