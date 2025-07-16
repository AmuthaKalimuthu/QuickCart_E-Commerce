package com.quickcart.entity;

import java.math.BigDecimal;
import javax.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
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
        private final Product p = new Product();
        public Builder id(Long id) { p.setId(id); return this; }
        public Builder name(String name) { p.setName(name); return this; }
        public Builder description(String description) { p.setDescription(description); return this; }
        public Builder price(BigDecimal price) { p.setPrice(price); return this; }
        public Builder imageUrl(String imageUrl) { p.setImageUrl(imageUrl); return this; }
        public Product build() { return p; }
    }
}