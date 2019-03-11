package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 12-08-2017.
 */
public class LeaveDetailsDataModel {

    @SerializedName("return")
    private String status;
    private String message;

    private LeaveDetailModel data;

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

    public LeaveDetailModel getData() {
        return data;
    }

    public void setData(LeaveDetailModel data) {
        this.data = data;
    }
}
