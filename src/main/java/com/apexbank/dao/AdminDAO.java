package com.apexbank.dao;

import com.apexbank.model.Admin;

/**
 * Data Access Object Interface for Admin entities.
 */
public interface AdminDAO {
    Admin findByUsername(String username);
    Admin findById(int adminId);
    boolean updateLastLogin(int adminId);
    boolean updatePassword(int adminId, String passwordHash);
}
