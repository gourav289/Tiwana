package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 18-12-2016.
 */
public class SelectAsmModel {
    @SerializedName("user_id")
    String id;
    @SerializedName("name")
    String AsmName;
    @SerializedName("state_name")
    String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAsmName() {
        return AsmName;
    }

    public void setAsmName(String asmName) {
        AsmName = asmName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
