package com.apexbank.service;

import com.apexbank.exception.BankingException;
import com.apexbank.model.Account;
import com.apexbank.model.AccountStatus;
import com.apexbank.model.Admin;
import com.apexbank.model.ATMCard;
import com.apexbank.model.AuditLog;
import com.apexbank.model.CardStatus;
import com.apexbank.model.Customer;
import com.apexbank.model.LoginHistory;
import com.apexbank.model.Transaction;

import java.util.List;
import java.util.Map;

/**
 * Service contract for Bank Administrative and Branch Management operations.
 */
public interface AdminService {
    Admin login(String username, String password, String ipAddress) throws BankingException;
    List<Customer> getAllCustomers();
    Customer getCustomerById(int customerId);
    int createCustomer(Customer customer) throws BankingException;
    boolean updateCustomer(Customer customer) throws BankingException;
    boolean deleteCustomer(int customerId) throws BankingException;

    List<Account> getAllAccounts();
    Account getAccountById(int accountId);
    int openAccountAndIssueCard(int customerId, String accountNumber, String accountType, double initialDeposit, String pin, String cardNumber, String cardType, String adminUser) throws BankingException;
    default int openAccountAndIssueCard(int customerId, String accountType, double initialDeposit, String pin, String cardType, String adminUser) throws BankingException {
        return openAccountAndIssueCard(customerId, null, accountType, initialDeposit, pin, null, cardType, adminUser);
    }
    boolean changeAccountStatus(int accountId, AccountStatus status, String adminUser) throws BankingException;

    List<ATMCard> getAllCards();
    boolean changeCardStatus(int cardId, CardStatus status, String adminUser) throws BankingException;
    boolean resetCardPinAttempts(int cardId, String adminUser) throws BankingException;

    List<Transaction> getAllRecentTransactions(int limit);
    Map<String, Object> getDashboardStatistics();
    List<AuditLog> getRecentAuditLogs(int limit);
    List<LoginHistory> getRecentLoginHistory(int limit);
}
