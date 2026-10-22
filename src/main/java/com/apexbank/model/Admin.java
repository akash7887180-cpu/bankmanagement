package com.apexbank.model;

import java.sql.Timestamp;

/**
 * Admin entity extending User.
 * Demonstrates OOP Inheritance and Method Overriding.
 */
public class Admin extends User {
    private static final long serialVersionUID = 1L;

    private int adminId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String role; // SUPER_ADMIN, BANK_MANAGER, AUDITOR
    private boolean isActive;
    private Timestamp lastLogin;

    public Admin() {
        super();
    }

    public Admin(int adminId, String username, String passwordHash, String fullName, 
                 String email, String role, boolean isActive) {
        super(email, "");
        this.adminId = adminId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.isActive = isActive;
    }

    @Override
    public String getDisplayName() {
        return fullName != null ? fullName : username;
    }

    @Override
    public String getRoleName() {
        return role != null ? role : "ADMIN";
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Timestamp getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Timestamp lastLogin) {
        this.lastLogin = lastLogin;
    }
}
