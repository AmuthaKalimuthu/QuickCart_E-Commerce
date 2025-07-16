package com.quickcart.dto;

import java.math.BigDecimal;

public class ProductResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String imageUrl;

    // Getters
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final ProductResponse pr = new ProductResponse();
        public Builder id(Long id) { pr.setId(id); return this; }
        public Builder name(String name) { pr.setName(name); return this; }
        public Builder description(String description) { pr.setDescription(description); return this; }
        public Builder price(BigDecimal price) { pr.setPrice(price); return this; }
        public Builder imageUrl(String imageUrl) { pr.setImageUrl(imageUrl); return this; }
        public ProductResponse build() { return pr; }
    }
}