package com.apexbank.service;

import com.apexbank.exception.BankingException;
import com.apexbank.model.Receipt;

/**
 * Interface defining fundamental ATM terminal operations.
 * Demonstrates Interface Segregation Principle (OOP).
 */
public interface AccountOperations {
    Receipt deposit(String cardNumber, double amount) throws BankingException;
    Receipt withdraw(String cardNumber, double amount) throws BankingException;
    Receipt fastCash(String cardNumber, double amount) throws BankingException;
    double getBalance(String cardNumber) throws BankingException;
}
