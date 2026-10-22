package com.apexbank.model;

import com.apexbank.exception.InsufficientFundsException;
import java.sql.Timestamp;

/**
 * Current Account implementation with Overdraft facility.
 * Demonstrates OOP Inheritance and Polymorphic Method Overriding.
 */
public class CurrentAccount extends Account {
    private static final long serialVersionUID = 1L;

    public CurrentAccount() {
        super();
        setMinimumBalance(5000.00);
        setInterestRate(0.00); // Current accounts yield no interest
        setOverdraftLimit(25000.00); // Default overdraft limit
    }

    public CurrentAccount(int accountId, String accountNumber, int customerId, double balance,
                          double minimumBalance, double overdraftLimit, AccountStatus status, Timestamp openedDate) {
        super(accountId, accountNumber, customerId, balance, minimumBalance, 0.00, overdraftLimit, status, openedDate);
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }

    @Override
    public double calculateInterest(int months) {
        return 0.0; // Current accounts typically earn zero interest
    }

    /**
     * Polymorphic implementation: allows withdrawal up to (balance + overdraftLimit).
     */
    @Override
    public boolean validateWithdrawal(double amount) throws InsufficientFundsException {
        double maxPermissibleWithdrawal = getBalance() + getOverdraftLimit();
        if (amount > maxPermissibleWithdrawal) {
            throw new InsufficientFundsException(amount, maxPermissibleWithdrawal);
        }
        return true;
    }
}
