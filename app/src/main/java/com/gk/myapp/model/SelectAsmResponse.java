package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 13-10-2017.
 */
public class SelectAsmResponse {
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

    public class Data{
        @SerializedName("total_target_weight")
        private String target;
        @SerializedName("total_achived_weight")
        private String achieved;
        @SerializedName("total_pending_weight")
        private String pending;

        @SerializedName("asm_detail")
        private List<SelectAsmModel> asmList;

        @SerializedName("so_detail")
        private List<SelectAsmModel> soList;

        @SerializedName("target_product_detail")
        private List<ProductDetailModel> productList;

        public List<ProductDetailModel> getProductList() {
            return productList;
        }

        public void setProductList(List<ProductDetailModel> productList) {
            this.productList = productList;
        }

        public List<SelectAsmModel> getSoList() {
            return soList;
        }

        public void setSoList(List<SelectAsmModel> soList) {
            this.soList = soList;
        }

        public String getTarget() {
            return target;
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getAchieved() {
            return achieved;
        }

        public void setAchieved(String achieved) {
            this.achieved = achieved;
        }

        public String getPending() {
            return pending;
        }

        public void setPending(String pending) {
            this.pending = pending;
        }

        public List<SelectAsmModel> getAsmList() {
            return asmList;
        }

        public void setAsmList(List<SelectAsmModel> asmList) {
            this.asmList = asmList;
        }
    }
}
