package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 12-10-2017.
 */
public class YearTargetResponse {

    @SerializedName("return")
    private String status;
    private String message;

    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
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

    public class Data {
        @SerializedName("first_half")
        private HalfData firstHalf;
        @SerializedName("second_half")
        private HalfData secondHalf;

        public HalfData getFirstHalf() {
            return firstHalf;
        }

        public void setFirstHalf(HalfData firstHalf) {
            this.firstHalf = firstHalf;
        }

        public HalfData getSecondHalf() {
            return secondHalf;
        }

        public void setSecondHalf(HalfData secondHalf) {
            this.secondHalf = secondHalf;
        }
    }

    public class HalfData {
        @SerializedName("Month")
        private String month;
        @SerializedName("target_status")
        private String targetStatus;

        public String getTargetStatus() {
            return targetStatus;
        }

        public void setTargetStatus(String targetStatus) {
            this.targetStatus = targetStatus;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }
    }
}
