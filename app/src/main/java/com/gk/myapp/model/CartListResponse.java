package com.gk.myapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Gk on 10-09-2017.
 */
public class CartListResponse {

    @SerializedName("return")
    private String status;
    private String message;

    private List<CartDetails> data;

    public List<CartDetails> getData() {
        return data;
    }

    public void setData(List<CartDetails> data) {
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

    public class CartDetails {
        @SerializedName("cart_id")
        private String cartId;
        @SerializedName("product_attr_id")
        private String productAttrId;
        private String name;
        private String image;
        private String quantity;
        private String description;
        private String article;
        private String weight;

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getProductAttrId() {
            return productAttrId;
        }

        public void setProductAttrId(String productAttrId) {
            this.productAttrId = productAttrId;
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

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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
