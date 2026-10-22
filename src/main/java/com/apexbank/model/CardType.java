package com.apexbank.model;

/**
 * Card scheme types.
 */
public enum CardType {
    DEBIT_RUPAY("RuPay Platinum"),
    DEBIT_VISA("Visa Signature"),
    DEBIT_MASTERCARD("MasterCard World");

    private final String displayName;

    CardType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static CardType fromString(String text) {
        for (CardType t : CardType.values()) {
            if (t.name().equalsIgnoreCase(text)) {
                return t;
            }
        }
        return DEBIT_RUPAY;
    }
}
