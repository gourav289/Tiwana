package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 27-08-2017.
 */
public class PartyListModel {
    @SerializedName("return")
    private String status;
    private String message;

    private List<PartyModel> data;

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

    public List<PartyModel> getData() {
        return data;
    }

    public void setData(List<PartyModel> data) {
        this.data = data;
    }
}
