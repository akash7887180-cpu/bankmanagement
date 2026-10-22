package com.apexbank.model;

/**
 * Status enumeration for bank accounts.
 */
public enum AccountStatus {
    ACTIVE,
    FROZEN,
    BLOCKED,
    CLOSED;

    public static AccountStatus fromString(String text) {
        for (AccountStatus s : AccountStatus.values()) {
            if (s.name().equalsIgnoreCase(text)) {
                return s;
            }
        }
        return ACTIVE;
    }
}
