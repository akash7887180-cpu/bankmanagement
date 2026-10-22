package com.apexbank.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Abstract base class representing a generic system user.
 * Demonstrates OOP Abstraction and Encapsulation.
 */
public abstract class User implements Serializable {
    private static final long serialVersionUID = 1L;

    private String email;
    private String phone;
    private Timestamp createdAt;

    public User() {
    }

    public User(String email, String phone) {
        this.email = email;
        this.phone = phone;
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Abstract methods to be implemented by Customer and Admin (Polymorphism / Abstraction)
    public abstract String getDisplayName();
    public abstract String getRoleName();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
