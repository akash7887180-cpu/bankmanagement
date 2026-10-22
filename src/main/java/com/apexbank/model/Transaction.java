package com.apexbank.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Transaction entity tracking all ledger movements.
 * Demonstrates OOP Encapsulation and Constructor Overloading.
 */
public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;

    private int transactionId;
    private String referenceNumber;
    private int accountId;
    private Integer cardId;
    private TransactionType transactionType;
    private double amount;
    private double balanceAfter;
    private Integer destinationAccountId;
    private String description;
    private String channel; // ATM, INTERNET_BANKING, FAST_CASH
    private String atmTerminalId;
    private String status;  // SUCCESS, FAILED, PENDING
    private Timestamp createdAt;

    // Additional transient fields for display convenience
    private String targetAccountNumber;
    private String sourceAccountNumber;

    // Default Constructor
    public Transaction() {
        this.status = "SUCCESS";
        this.channel = "ATM";
        this.createdAt = new Timestamp(System.currentTimeMillis());
    }

    // Constructor Overloading (1): Core creation
    public Transaction(String referenceNumber, int accountId, Integer cardId,
                       TransactionType transactionType, double amount, double balanceAfter,
                       String description, String atmTerminalId) {
        this();
        this.referenceNumber = referenceNumber;
        this.accountId = accountId;
        this.cardId = cardId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.description = description;
        this.atmTerminalId = atmTerminalId;
    }

    // Constructor Overloading (2): Transfer transaction
    public Transaction(String referenceNumber, int accountId, Integer cardId,
                       TransactionType transactionType, double amount, double balanceAfter,
                       Integer destinationAccountId, String description, String atmTerminalId) {
        this(referenceNumber, accountId, cardId, transactionType, amount, balanceAfter, description, atmTerminalId);
        this.destinationAccountId = destinationAccountId;
    }

    // Constructor Overloading (3): Full Hydration from JDBC
    public Transaction(int transactionId, String referenceNumber, int accountId, Integer cardId,
                       TransactionType transactionType, double amount, double balanceAfter,
                       Integer destinationAccountId, String description, String channel,
                       String atmTerminalId, String status, Timestamp createdAt) {
        this.transactionId = transactionId;
        this.referenceNumber = referenceNumber;
        this.accountId = accountId;
        this.cardId = cardId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.destinationAccountId = destinationAccountId;
        this.description = description;
        this.channel = channel;
        this.atmTerminalId = atmTerminalId;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getFormattedDate() {
        if (createdAt == null) return "";
        return new SimpleDateFormat("dd MMM yyyy, hh:mm:ss a").format(createdAt);
    }

    public String getFormattedDateOnly() {
        if (createdAt == null) return "";
        return new SimpleDateFormat("dd-MM-yyyy").format(createdAt);
    }

    public String getFormattedTimeOnly() {
        if (createdAt == null) return "";
        return new SimpleDateFormat("hh:mm:ss a").format(createdAt);
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getBalanceAfter() {
        return balanceAfter;
    }

    public void setBalanceAfter(double balanceAfter) {
        this.balanceAfter = balanceAfter;
    }

    public Integer getDestinationAccountId() {
        return destinationAccountId;
    }

    public void setDestinationAccountId(Integer destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getAtmTerminalId() {
        return atmTerminalId;
    }

    public void setAtmTerminalId(String atmTerminalId) {
        this.atmTerminalId = atmTerminalId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    public void setSourceAccountNumber(String sourceAccountNumber) {
        this.sourceAccountNumber = sourceAccountNumber;
    }
}
