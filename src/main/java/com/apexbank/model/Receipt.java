package com.apexbank.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Encapsulates the detailed data printed on a physical ATM thermal slip.
 * Demonstrates OOP Encapsulation and Data Transfer Object (DTO) pattern.
 */
public class Receipt implements Serializable {
    private static final long serialVersionUID = 1L;

    private int receiptId;
    private String receiptNumber;
    private int transactionId;
    private String transactionReference;
    private String bankName;
    private String branchName;
    private String ifscCode;
    private String atmId;
    private String terminalId;
    private String maskedCard;
    private String maskedAccount;
    private String customerName;
    private TransactionType transactionType;
    private double amount;
    private double availableBalance;
    private String customerCarePhone;
    private String thankYouMessage;
    private Timestamp timestamp;

    public Receipt() {
        this.bankName = "Apex National Banking Corporation";
        this.branchName = "Connaught Place Financial Center, New Delhi";
        this.ifscCode = "APEX0001042";
        this.atmId = "APEX-ATM-042";
        this.terminalId = "TERM-DEL-08";
        this.customerCarePhone = "1800-420-APEX (2739)";
        this.thankYouMessage = "Thank you for banking with Apex National Bank. Save Paper, Save Trees.";
        this.timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getFormattedDate() {
        if (timestamp == null) return "";
        return new SimpleDateFormat("dd/MM/yyyy").format(timestamp);
    }

    public String getFormattedTime() {
        if (timestamp == null) return "";
        return new SimpleDateFormat("HH:mm:ss").format(timestamp);
    }

    public String getFormattedDateTime() {
        if (timestamp == null) return "";
        return new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(timestamp);
    }

    // Getters and Setters
    public int getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(int receiptId) {
        this.receiptId = receiptId;
    }

    public String getReceiptNumber() {
        return receiptNumber;
    }

    public void setReceiptNumber(String receiptNumber) {
        this.receiptNumber = receiptNumber;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public String getAtmId() {
        return atmId;
    }

    public void setAtmId(String atmId) {
        this.atmId = atmId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public void setMaskedCard(String maskedCard) {
        this.maskedCard = maskedCard;
    }

    public String getMaskedAccount() {
        return maskedAccount;
    }

    public void setMaskedAccount(String maskedAccount) {
        this.maskedAccount = maskedAccount;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public double getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getCustomerCarePhone() {
        return customerCarePhone;
    }

    public void setCustomerCarePhone(String customerCarePhone) {
        this.customerCarePhone = customerCarePhone;
    }

    public String getThankYouMessage() {
        return thankYouMessage;
    }

    public void setThankYouMessage(String thankYouMessage) {
        this.thankYouMessage = thankYouMessage;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
