package com.apexbank.model;

/**
 * Types of ATM and banking transactions.
 */
public enum TransactionType {
    DEPOSIT("Cash Deposit", true),
    WITHDRAWAL("Cash Withdrawal", false),
    FAST_CASH("Fast Cash Dispense", false),
    TRANSFER_OUT("Fund Transfer (Debit)", false),
    TRANSFER_IN("Fund Transfer (Credit)", true),
    BALANCE_ENQUIRY("Balance Enquiry", false),
    PIN_CHANGE("ATM PIN Change", false);

    private final String description;
    private final boolean isCredit;

    TransactionType(String description, boolean isCredit) {
        this.description = description;
        this.isCredit = isCredit;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCredit() {
        return isCredit;
    }

    public static TransactionType fromString(String text) {
        for (TransactionType t : TransactionType.values()) {
            if (t.name().equalsIgnoreCase(text)) {
                return t;
            }
        }
        return WITHDRAWAL;
    }
}
