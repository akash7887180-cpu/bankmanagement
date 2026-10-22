package com.apexbank.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Tracks login attempts for security auditing.
 */
public class LoginHistory implements Serializable {
    private static final long serialVersionUID = 1L;

    private int historyId;
    private String userType; // CUSTOMER, ADMIN
    private String identifier;
    private String ipAddress;
    private String userAgent;
    private String status; // SUCCESS, FAILED_PIN, ACCOUNT_LOCKED, FAILED_AUTH
    private String failureReason;
    private Timestamp timestamp;

    public LoginHistory() {
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public LoginHistory(String userType, String identifier, String ipAddress, 
                        String userAgent, String status, String failureReason) {
        this();
        this.userType = userType;
        this.identifier = identifier;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.status = status;
        this.failureReason = failureReason;
    }

    public String getFormattedDate() {
        if (timestamp == null) return "";
        return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(timestamp);
    }

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
