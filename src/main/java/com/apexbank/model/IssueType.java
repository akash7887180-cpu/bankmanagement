package com.apexbank.model;

/**
 * Types of financial issues and customer grievances addressed by Apex Bank.
 */
public enum IssueType {
    FAILED_ATM_CASH("ATM Cash Not Dispensed but Debited"),
    LOST_CARD_FRAUD("Lost / Stolen Card & Emergency Lock"),
    TRANSACTION_DISPUTE("Unauthorized or Disputed Transaction"),
    ACCOUNT_FROZEN("Frozen / Blocked Account Reactivation"),
    EMI_HARDSHIP("Loan & EMI Financial Assistance"),
    GENERAL_INQUIRY("General Banking & Problem Solving");

    private final String displayName;

    IssueType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
