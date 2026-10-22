package com.apexbank.service;

import com.apexbank.exception.BankingException;
import com.apexbank.model.Account;
import com.apexbank.model.ATMCard;
import com.apexbank.model.Customer;
import com.apexbank.model.Receipt;
import com.apexbank.model.Transaction;

import java.util.List;

/**
 * High-level ATM and Self-Service Internet Banking service contract.
 */
public interface ATMService extends AccountOperations {
    ATMCard authenticate(String cardNumber, String enteredPin, String ipAddress, String userAgent) throws BankingException;
    Receipt transfer(String sourceCardNumber, String targetAccountNumber, double amount, String description) throws BankingException;
    List<Transaction> getMiniStatement(String cardNumber) throws BankingException;
    List<Transaction> getFullStatement(String cardNumber, String type, String startDate, String endDate, String search) throws BankingException;
    boolean changePin(String cardNumber, String oldPin, String newPin) throws BankingException;
    Customer getCustomerByCard(String cardNumber) throws BankingException;
    Account getAccountByCard(String cardNumber) throws BankingException;
    Receipt generateReceipt(Transaction txn, Account acc, Customer cust, ATMCard card);
    boolean verifyOtp(String phone, String otp);
    String triggerOtp(String phone, String purpose);
    double getRemainingDailyLimit(String cardNumber) throws BankingException;
}
