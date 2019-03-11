package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 30-08-2017.
 */
public class PaymentHistoryResponse {
    @SerializedName("return")
    private String status;
    private String message;
    private List<PaymentHistoryModel> data;

    public List<PaymentHistoryModel> getData() {
        return data;
    }

    public void setData(List<PaymentHistoryModel> data) {
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
