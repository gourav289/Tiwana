package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 09-09-2017.
 */
public class StateResponseModel {
    @SerializedName("return")
    private String status;
    private String message;

    public List<StateDetails> getData() {
        return data;
    }

    public void setData(List<StateDetails> data) {
        this.data = data;
    }

    private List<StateDetails> data;


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

    public class StateDetails {
        private String id;
        private String name;
        @SerializedName("state_code")
        private String stateCode;

        public StateDetails(String id, String name){
            this.id=id;
            this.name=name;
        }

        public String getStateCode() {
            return stateCode;
        }

        public void setStateCode(String stateCode) {
            this.stateCode = stateCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

}
