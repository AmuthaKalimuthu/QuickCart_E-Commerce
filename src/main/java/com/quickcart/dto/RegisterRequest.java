package com.quickcart.dto;

public class RegisterRequest {
    private String username;
    private String password;
    private String role; // "ADMIN" or "CUSTOMER"

    // Add getters
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }

    // Optionally, add setters if you need them (for deserialization)
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
}