package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 27-08-2017.
 */
public class PartyModel {
    @SerializedName("party_id")
    private String partyId;
    @SerializedName("party_name")
    private String partyName;
    @SerializedName("user_id")
    private String userId;

    @SerializedName("outstanding_payment")
    private String outstandingPayment;

    public String getOutstandingPayment() {
        return outstandingPayment;
    }

    public void setOutstandingPayment(String outstandingPayment) {
        this.outstandingPayment = outstandingPayment;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
