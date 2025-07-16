package com.quickcart.dto;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
    private Long id;
    private List<CartItemResponse> items;
    private BigDecimal total;

    // Getters
    public Long getId() { return id; }
    public List<CartItemResponse> getItems() { return items; }
    public BigDecimal getTotal() { return total; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItems(List<CartItemResponse> items) { this.items = items; }
    public void setTotal(BigDecimal total) { this.total = total; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final CartResponse cr = new CartResponse();
        public Builder id(Long id) { cr.setId(id); return this; }
        public Builder items(List<CartItemResponse> items) { cr.setItems(items); return this; }
        public Builder total(BigDecimal total) { cr.setTotal(total); return this; }
        public CartResponse build() { return cr; }
    }
}
