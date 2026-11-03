package com.apexbank.dao;

import com.apexbank.model.Loan;
import com.apexbank.model.LoanStatus;
import java.util.List;

public interface LoanDAO {
    boolean create(Loan loan);
    Loan findById(int loanId);
    Loan findByReference(String loanReference);
    List<Loan> findByCustomerId(int customerId);
    List<Loan> findByAccountId(int accountId);
    List<Loan> findAll();
    boolean updateStatus(int loanId, LoanStatus status, String adminRemarks);
    boolean markDisbursed(int loanId, String adminRemarks);
    int countPending();
}
