package com.quickcart.entity;

import javax.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }

    // Builder
    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private final User user = new User();
        public Builder id(Long id) { user.setId(id); return this; }
        public Builder username(String username) { user.setUsername(username); return this; }
        public Builder password(String password) { user.setPassword(password); return this; }
        public Builder role(Role role) { user.setRole(role); return this; }
        public User build() { return user; }
    }
}