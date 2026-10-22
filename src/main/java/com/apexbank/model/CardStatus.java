package com.apexbank.model;

/**
 * Status enumeration for ATM debit cards.
 */
public enum CardStatus {
    ACTIVE,
    LOCKED,
    BLOCKED,
    EXPIRED;

    public static CardStatus fromString(String text) {
        for (CardStatus s : CardStatus.values()) {
            if (s.name().equalsIgnoreCase(text)) {
                return s;
            }
        }
        return ACTIVE;
    }
}
