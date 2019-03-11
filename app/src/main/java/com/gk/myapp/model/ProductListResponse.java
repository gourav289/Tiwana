package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Gk on 10-09-2017.
 */
public class ProductListResponse {

    @SerializedName("return")
    private String status;
    private String message;

    private List<ProductDetails> data;

    public List<ProductDetails> getData() {
        return data;
    }

    public void setData(List<ProductDetails> data) {
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


    public class ProductDetails implements Serializable {
        @SerializedName("product_id")
        private String id;
        private String name;
        private String image;
        private String price;
        private String article;
        private String description;
        private String weight;
        private int quantity;
        private List<ProductAttributes> attribute;

        public List<ProductAttributes> getAttribute() {
            return attribute;
        }

        public void setAttribute(List<ProductAttributes> attribute) {
            this.attribute = attribute;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }

    public class ProductAttributes {
        @SerializedName("attribute_id")
        private String attributeId;
        @SerializedName("product_id")
        private String productId;
        private String article;
        private String weight;
        private int quantity = 1;

        public int getQuantity() {
            if (quantity == 0)
                quantity = 1;
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getAttributeId() {
            return attributeId;
        }

        public void setAttributeId(String attributeId) {
            this.attributeId = attributeId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public String getArticle() {
            return article;
        }

        public void setArticle(String article) {
            this.article = article;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }
    }
}
