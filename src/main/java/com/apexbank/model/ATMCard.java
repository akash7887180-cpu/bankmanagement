package com.apexbank.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

/**
 * ATM Card entity representing an electronic access card.
 * Demonstrates OOP Encapsulation and Business Rule Invariants.
 */
public class ATMCard implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final int MAX_FAILED_ATTEMPTS = 3;

    private int cardId;
    private String cardNumber;
    private int accountId;
    private int customerId;
    private CardType cardType;
    private String pinHash;
    private String pinSalt;
    private Date expiryDate;
    private String cvvHash;
    private double dailyLimit;
    private int failedPinAttempts;
    private CardStatus status;
    private Timestamp issuedAt;
    private String pinPlain;

    public ATMCard() {
        this.status = CardStatus.ACTIVE;
        this.cardType = CardType.DEBIT_RUPAY;
        this.dailyLimit = 40000.00;
        this.failedPinAttempts = 0;
    }

    public ATMCard(int cardId, String cardNumber, int accountId, int customerId,
                   CardType cardType, String pinHash, String pinSalt, Date expiryDate,
                   String cvvHash, double dailyLimit, int failedPinAttempts, CardStatus status,
                   Timestamp issuedAt) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        this.accountId = accountId;
        this.customerId = customerId;
        this.cardType = cardType;
        this.pinHash = pinHash;
        this.pinSalt = pinSalt;
        this.expiryDate = expiryDate;
        this.cvvHash = cvvHash;
        this.dailyLimit = dailyLimit;
        this.failedPinAttempts = failedPinAttempts;
        this.status = status;
        this.issuedAt = issuedAt;
    }

    /**
     * Increments failed attempts and locks the card if threshold reached.
     * @return true if card has become locked as a result.
     */
    public boolean incrementFailedAttempts() {
        this.failedPinAttempts++;
        if (this.failedPinAttempts >= MAX_FAILED_ATTEMPTS) {
            this.status = CardStatus.LOCKED;
            return true;
        }
        return false;
    }

    public void resetFailedAttempts() {
        this.failedPinAttempts = 0;
    }

    public boolean isLocked() {
        return this.status == CardStatus.LOCKED || this.failedPinAttempts >= MAX_FAILED_ATTEMPTS;
    }

    public boolean isUsable() {
        return this.status == CardStatus.ACTIVE && !isLocked();
    }

    /**
     * Returns masked card format: XXXX-XXXX-XXXX-1234
     */
    public String getMaskedCardNumber() {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "XXXX-XXXX-XXXX-XXXX";
        }
        String clean = cardNumber.replaceAll("\\s+", "");
        if (clean.length() == 16) {
            return clean.substring(0, 4) + "-XXXX-XXXX-" + clean.substring(12);
        }
        return "XXXX-XXXX-XXXX-" + clean.substring(Math.max(0, clean.length() - 4));
    }

    // Getters and Setters
    public int getCardId() {
        return cardId;
    }

    public void setCardId(int cardId) {
        this.cardId = cardId;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public String getPinHash() {
        return pinHash;
    }

    public void setPinHash(String pinHash) {
        this.pinHash = pinHash;
    }

    public String getPinSalt() {
        return pinSalt;
    }

    public void setPinSalt(String pinSalt) {
        this.pinSalt = pinSalt;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvvHash() {
        return cvvHash;
    }

    public void setCvvHash(String cvvHash) {
        this.cvvHash = cvvHash;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }

    public void setDailyLimit(double dailyLimit) {
        this.dailyLimit = dailyLimit;
    }

    public int getFailedPinAttempts() {
        return failedPinAttempts;
    }

    public void setFailedPinAttempts(int failedPinAttempts) {
        this.failedPinAttempts = failedPinAttempts;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public Timestamp getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(Timestamp issuedAt) {
        this.issuedAt = issuedAt;
    }

    public String getPinPlain() {
        return (pinPlain != null && !pinPlain.trim().isEmpty()) ? pinPlain.trim() : "1234";
    }

    public void setPinPlain(String pinPlain) {
        this.pinPlain = pinPlain;
    }

    /**
     * Formats card number into 4-4-4-4 chunked format for UI clarity: 4532 1234 5678 9012
     */
    public String getFormattedCardNumber() {
        if (cardNumber == null) return "";
        String clean = cardNumber.replaceAll("\\D", "");
        if (clean.length() == 16) {
            return clean.substring(0, 4) + " " + clean.substring(4, 8) + " " + clean.substring(8, 12) + " " + clean.substring(12, 16);
        }
        return cardNumber;
    }
}
