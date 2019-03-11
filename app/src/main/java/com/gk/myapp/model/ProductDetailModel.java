package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Gk on 18-12-2016.
 */
public class ProductDetailModel {
    @SerializedName("product_id")
    private String id;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("quantity")
    private String totalQuantity;
    @SerializedName("achived")
    private String soldQuantity;
    private String quantityKg;
    @SerializedName("product_image")
    private String productImage;

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuantityNo() {
        return quantityNo;
    }

    public void setQuantityNo(String quantityNo) {
        this.quantityNo = quantityNo;
    }

    public String getQuantityKg() {
        return quantityKg;
    }

    public void setQuantityKg(String quantityKg) {
        this.quantityKg = quantityKg;
    }

    private String quantityNo;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(String totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(String soldQuantity) {
        this.soldQuantity = soldQuantity;
    }
}
