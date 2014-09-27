package com.halo.app.core.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterDevice implements Serializable {

    @SerializedName("success")
    private boolean success;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
