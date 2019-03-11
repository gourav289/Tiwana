package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 29-08-2017.
 */
public class NotificationResponseModel {

    @SerializedName("return")
    private String status;
    private String message;
    private List<NotificationModel> data;

    public List<NotificationModel> getData() {
        return data;
    }

    public void setData(List<NotificationModel> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
