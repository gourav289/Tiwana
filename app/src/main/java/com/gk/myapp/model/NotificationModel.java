package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 18-12-2016.
 */
public class NotificationModel {
    @SerializedName("id")
    private String id;
    private String message;
    @SerializedName("created_at")
    private String date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
