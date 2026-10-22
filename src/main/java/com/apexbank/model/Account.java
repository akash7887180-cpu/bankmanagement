package com.apexbank.model;

import com.apexbank.exception.InsufficientFundsException;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Abstract class representing a general Bank Account.
 * Demonstrates OOP Abstraction, Encapsulation, Constructor Overloading, and Method Overloading.
 */
public abstract class Account implements Serializable {
    private static final long serialVersionUID = 1L;

    private int accountId;
    private String accountNumber;
    private int customerId;
    private double balance;
    private double minimumBalance;
    private double interestRate;
    private double overdraftLimit;
    private AccountStatus status;
    private Timestamp openedDate;

    // Default constructor
    public Account() {
        this.status = AccountStatus.ACTIVE;
        this.openedDate = new Timestamp(System.currentTimeMillis());
    }

    // Constructor Overloading (1)
    public Account(String accountNumber, int customerId, double balance) {
        this();
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
    }

    // Constructor Overloading (2)
    public Account(int accountId, String accountNumber, int customerId, double balance,
                   double minimumBalance, double interestRate, double overdraftLimit,
                   AccountStatus status, Timestamp openedDate) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.customerId = customerId;
        this.balance = balance;
        this.minimumBalance = minimumBalance;
        this.interestRate = interestRate;
        this.overdraftLimit = overdraftLimit;
        this.status = status;
        this.openedDate = openedDate;
    }

    // Abstract methods to be overridden by subclasses (Polymorphism)
    public abstract String getAccountType();
    public abstract double calculateInterest(int months);
    public abstract boolean validateWithdrawal(double amount) throws InsufficientFundsException;

    // Encapsulated state manipulation methods
    public synchronized void credit(double amount) {
        if (amount > 0) {
            this.balance += amount;
        }
    }

    public synchronized void debit(double amount) throws InsufficientFundsException {
        if (validateWithdrawal(amount)) {
            this.balance -= amount;
        }
    }

    // Method Overloading (1)
    public void deposit(double amount) {
        credit(amount);
    }

    // Method Overloading (2)
    public void deposit(double amount, String transactionNote) {
        credit(amount);
        // Note handled by service/transaction record
    }

    public boolean isActive() {
        return this.status == AccountStatus.ACTIVE;
    }

    // Getters and Setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getMinimumBalance() {
        return minimumBalance;
    }

    public void setMinimumBalance(double minimumBalance) {
        this.minimumBalance = minimumBalance;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }

    public double getOverdraftLimit() {
        return overdraftLimit;
    }

    public void setOverdraftLimit(double overdraftLimit) {
        this.overdraftLimit = overdraftLimit;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    public Timestamp getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(Timestamp openedDate) {
        this.openedDate = openedDate;
    }
}
