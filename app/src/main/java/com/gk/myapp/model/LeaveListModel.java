package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 08-08-2017.
 */
public class LeaveListModel {
    @SerializedName("return")
    private String status;
    private String message;
    private List<LeaveDetailModel> data;

    public List<LeaveDetailModel> getData() {
        return data;
    }

    public void setData(List<LeaveDetailModel> data) {
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
