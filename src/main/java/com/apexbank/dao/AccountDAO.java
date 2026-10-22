package com.apexbank.dao;

import com.apexbank.model.Account;
import com.apexbank.model.AccountStatus;

import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object Interface for Bank Accounts.
 * Demonstrates Interface Abstraction and Method Overloading.
 */
public interface AccountDAO {
    Account findById(int accountId);
    Account findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(int customerId);
    List<Account> findAll();
    int create(Account account);
    boolean updateBalance(int accountId, double newBalance, Connection conn);
    boolean updateStatus(int accountId, AccountStatus status);
    double getTotalBankBalance();
    int countTotalAccounts();
}
