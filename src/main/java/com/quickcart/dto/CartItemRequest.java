package com.quickcart.dto;

public class CartItemRequest {
    private Long productId;
    private int quantity;

    // Getter for productId
    public Long getProductId() {
        return productId;
    }

    // Setter for productId
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    // Getter for quantity
    public int getQuantity() {
        return quantity;
    }

    // Setter for quantity
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}