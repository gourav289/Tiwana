package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 31-01-2017.
 */
public class PaymentHistoryModel {
    @SerializedName("payment_mode")
    private String title;
    @SerializedName("submited_at")
    private String date;
    private String amount;
    @SerializedName("party_name")
    private String partyName;
    @SerializedName("transaction_id")
    private String transactionId;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
