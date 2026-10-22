package com.apexbank.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * System audit trail log entity.
 */
public class AuditLog implements Serializable {
    private static final long serialVersionUID = 1L;

    private int logId;
    private String action;
    private String performedBy;
    private String details;
    private String ipAddress;
    private Timestamp createdAt;

    public AuditLog() {
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    public AuditLog(String action, String performedBy, String details, String ipAddress) {
        this();
        this.action = action;
        this.performedBy = performedBy;
        this.details = details;
        this.ipAddress = ipAddress;
    }

    public String getFormattedDate() {
        if (createdAt == null) return "";
        return new SimpleDateFormat("dd MMM yyyy HH:mm:ss").format(createdAt);
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPerformedBy() {
        return performedBy;
    }

    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
