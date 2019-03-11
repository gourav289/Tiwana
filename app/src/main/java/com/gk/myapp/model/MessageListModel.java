package com.gk.myapp.model;

import android.os.Message;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 31-07-2017.
 */
public class MessageListModel {
    @SerializedName("return")
    private String status;
    private String message;
    private List<MessageModel> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<MessageModel> getData() {
        return data;
    }

    public void setData(List<MessageModel> data) {
        this.data = data;
    }
}
