package com.apexbank.dao;

import com.apexbank.model.AuditLog;
import com.apexbank.model.LoginHistory;
import com.apexbank.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * JDBC Implementation of AuditDAO.
 */
public class AuditDAOImpl implements AuditDAO {

    @Override
    public void log(String action, String performedBy, String details, String ipAddress) {
        String sql = "INSERT INTO audit_logs (action, performedBy, details, ip_address) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, action);
            ps.setString(2, performedBy != null ? performedBy : "ANONYMOUS");
            ps.setString(3, details);
            ps.setString(4, ipAddress);
            ps.executeUpdate();
        } catch (SQLException e) {
            // Audit logging shouldn't crash operations, but print error
            System.err.println("Audit log failure: " + e.getMessage());
        }
    }

    @Override
    public void logLogin(LoginHistory history) {
        String sql = "INSERT INTO login_history (user_type, identifier, ip_address, user_agent, status, failure_reason) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, history.getUserType());
            ps.setString(2, history.getIdentifier());
            ps.setString(3, history.getIpAddress());
            ps.setString(4, history.getUserAgent());
            ps.setString(5, history.getStatus());
            ps.setString(6, history.getFailureReason());
            ps.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Login history failure: " + e.getMessage());
        }
    }

    @Override
    public List<AuditLog> findRecentLogs(int limit) {
        List<AuditLog> list = new ArrayList<>();
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 20);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    AuditLog log = new AuditLog();
                    log.setLogId(rs.getInt("log_id"));
                    log.setAction(rs.getString("action"));
                    log.setPerformedBy(rs.getString("performedBy"));
                    log.setDetails(rs.getString("details"));
                    log.setIpAddress(rs.getString("ip_address"));
                    log.setCreatedAt(rs.getTimestamp("created_at"));
                    list.add(log);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<LoginHistory> findRecentLogins(int limit) {
        List<LoginHistory> list = new ArrayList<>();
        String sql = "SELECT * FROM login_history ORDER BY timestamp DESC LIMIT ?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit > 0 ? limit : 20);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LoginHistory h = new LoginHistory();
                    h.setHistoryId(rs.getInt("history_id"));
                    h.setUserType(rs.getString("user_type"));
                    h.setIdentifier(rs.getString("identifier"));
                    h.setIpAddress(rs.getString("ip_address"));
                    h.setUserAgent(rs.getString("user_agent"));
                    h.setStatus(rs.getString("status"));
                    h.setFailureReason(rs.getString("failure_reason"));
                    h.setTimestamp(rs.getTimestamp("timestamp"));
                    list.add(h);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
