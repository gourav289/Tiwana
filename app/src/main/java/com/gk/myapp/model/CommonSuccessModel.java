package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 20-02-2017.
 */
public class CommonSuccessModel {

    @SerializedName("return")
    private String success;
    @SerializedName("message")
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
