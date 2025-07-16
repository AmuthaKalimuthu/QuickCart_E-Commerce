package com.quickcart.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private List<OrderItemResponse> items;
    private BigDecimal total;
    private LocalDateTime createdAt;

    // Getters
    public Long getId() { return id; }
    public List<OrderItemResponse> getItems() { return items; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }
    public void setTotal(BigDecimal total) { this.total = total; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final OrderResponse or = new OrderResponse();
        public Builder id(Long id) { or.setId(id); return this; }
        public Builder items(List<OrderItemResponse> items) { or.setItems(items); return this; }
        public Builder total(BigDecimal total) { or.setTotal(total); return this; }
        public Builder createdAt(LocalDateTime createdAt) { or.setCreatedAt(createdAt); return this; }
        public OrderResponse build() { return or; }
    }
}