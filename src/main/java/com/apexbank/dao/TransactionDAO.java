package com.apexbank.dao;

import com.apexbank.model.Transaction;
import java.sql.Connection;
import java.util.List;

/**
 * Data Access Object Interface for Financial Transactions.
 */
public interface TransactionDAO {
    int create(Transaction txn, Connection conn);
    int create(Transaction txn);
    Transaction findById(int transactionId);
    Transaction findByReference(String referenceNumber);
    List<Transaction> findLastTransactions(int accountId, int limit);
    List<Transaction> findAllByAccount(int accountId);
    List<Transaction> findFiltered(int accountId, String type, String startDate, String endDate, String search);
    List<Transaction> findAllRecent(int limit);
    double getTotalDeposits();
    double getTotalWithdrawals();
    int getTodayTransactionCount();
    double getTodayVolume();
}
