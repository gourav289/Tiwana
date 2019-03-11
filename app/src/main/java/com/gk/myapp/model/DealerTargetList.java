package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 31-01-2018.
 */
public class DealerTargetList {
    @SerializedName("return")
    private String success;
    @SerializedName("message")
    private String message;
    private List<MonthList> data;

    public List<MonthList> getData() {
        return data;
    }

    public void setData(List<MonthList> data) {
        this.data = data;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class MonthList {
        @SerializedName("Month")
        private String month;
        @SerializedName("Month_detail")
        private String monthDetail;
        @SerializedName("target_status")
        private String targetStatus;

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        private String year;

        public String getMonthDetail() {
            return monthDetail;
        }

        public void setMonthDetail(String monthDetail) {
            this.monthDetail = monthDetail;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getTargetStatus() {
            return targetStatus;
        }

        public void setTargetStatus(String targetStatus) {
            this.targetStatus = targetStatus;
        }
    }
}
