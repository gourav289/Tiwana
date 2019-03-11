package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gk on 02-02-2018.
 */
public class OrderHistoryResponse implements Serializable {

    @SerializedName("return")
    private String status;
    private String message;
    @SerializedName("total_count")
    private String totalRecords;

    public String getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(String totalRecords) {
        this.totalRecords = totalRecords;
    }

    private List<OrderHistoryList> data;

    public List<OrderHistoryList> getData() {
        return data;
    }

    public void setData(List<OrderHistoryList> data) {
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

    public class OrderHistoryList {
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("user_id")
        private String userId;
        private String year;
        private String month;
        @SerializedName("created_for")
        private String createdFor;
        @SerializedName("created_date")
        private String createDate;
        private String isReadByTally;
        private String status;
        private String name;
        @SerializedName("party_name")
        private String partyName;
        private String image;

        private String quantity;

        @SerializedName("product_name")
        private String productName;

        @SerializedName("order_detail")
        private List<OrderDetails> orderDetail;

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public List<OrderDetails> getOrderDetail() {
            return orderDetail;
        }

        public void setOrderDetail(List<OrderDetails> orderDetail) {
            this.orderDetail = orderDetail;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getCreatedFor() {
            return createdFor;
        }

        public void setCreatedFor(String createdFor) {
            this.createdFor = createdFor;
        }

        public String getCreateDate() {
            return createDate;
        }

        public void setCreateDate(String createDate) {
            this.createDate = createDate;
        }

        public String getIsReadByTally() {
            return isReadByTally;
        }

        public void setIsReadByTally(String isReadByTally) {
            this.isReadByTally = isReadByTally;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPartyName() {
            return partyName;
        }

        public void setPartyName(String partyName) {
            this.partyName = partyName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

    public class OrderDetails implements Serializable {

        @SerializedName("order_detail_id")
        private String orderId;
        @SerializedName("user_id")
        private String userId;
        @SerializedName("product_attr_id")
        private String productAttibId;
        private String article;
        private String quantity;
        @SerializedName("product_id")
        private String productId;
        private String weight;
        @SerializedName("product_name")
        private String productName;
        @SerializedName("product_image")
        private String productImage;
        @SerializedName("product_description")
        private String productDesc;
        private String period;
        private String year;
        private String month;
        @SerializedName("created_date")
        private String createdDate;

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getProductAttibId() {
            return productAttibId;
        }

        public void setProductAttibId(String productAttibId) {
            this.productAttibId = productAttibId;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductImage() {
            return productImage;
        }

        public void setProductImage(String productImage) {
            this.productImage = productImage;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }
    }
}
