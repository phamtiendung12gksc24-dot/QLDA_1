package com.example.duan1.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("_id")
    private String id;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("product_id")
    private Product product;
    private int quantity;
    private double subtotal;

    public CartItem() {
    }

    public CartItem(String id, String userId, Product product, int quantity, double subtotal) {
        this.id = id;
        this.userId = userId;
        this.product = product;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getFormattedSubtotal() {
        long subtotalLong = (long) subtotal;
        if (subtotalLong >= 1000) {
            long thousands = subtotalLong / 1000;
            return String.format("%,d.000đ", thousands).replace(",", ".");
        } else {
            return String.format("%d.000đ", subtotalLong);
        }
    }
}

