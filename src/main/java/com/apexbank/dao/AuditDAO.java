package com.apexbank.dao;

import com.apexbank.model.AuditLog;
import com.apexbank.model.LoginHistory;

import java.util.List;

/**
 * Data Access Object Interface for Audit Logs and Security History.
 */
public interface AuditDAO {
    void log(String action, String performedBy, String details, String ipAddress);
    void logLogin(LoginHistory history);
    List<AuditLog> findRecentLogs(int limit);
    List<LoginHistory> findRecentLogins(int limit);
}
