# Sequence Diagram - Apex ATM Banking Management System

This document visualizes the dynamic execution flows for two critical banking transactions: **ATM Cash Withdrawal** and **Atomic Inter-Account Fund Transfer**.

---

## 1. ATM Cash Withdrawal Sequence

```mermaid
sequenceDiagram
    autonumber
    actor Customer as 👤 Customer
    participant Kiosk as 🖥️ WithdrawServlet
    participant Service as ⚙️ ATMServiceImpl
    participant CardDAO as 💳 ATMCardDAOImpl
    participant Account as 🏦 SavingsAccount (Polymorphic)
    participant AccDAO as 🗄️ AccountDAOImpl
    participant TxnDAO as 📝 TransactionDAOImpl
    participant DB as 💾 MySQL Database

    Customer->>Kiosk: Enter Amount (e.g. ₹5,000) & Submit
    Kiosk->>Service: withdraw(cardNumber, 5000.00)
    
    Service->>CardDAO: getTodayWithdrawalTotal(cardId)
    CardDAO->>DB: SELECT SUM(amount) WHERE today
    DB-->>CardDAO: Returns ₹2,000.00
    CardDAO-->>Service: ₹2,000.00 used (Remaining: ₹38,000.00)
    
    Note over Service,Account: Polymorphic Business Invariant Check
    Service->>Account: validateWithdrawal(5000.00)
    alt Balance - Amount < Minimum Balance (₹1,000)
        Account-->>Service: throw InsufficientFundsException
        Service-->>Kiosk: Show Error Message
        Kiosk-->>Customer: Transaction Declined: Insufficient Funds
    else Funds Permitted
        Account-->>Service: Validation Successful
        
        Note over Service,DB: JDBC Transaction: setAutoCommit(false)
        Service->>DB: Begin ACID Transaction
        Service->>Account: debit(5000.00)
        Service->>AccDAO: updateBalance(accountId, newBal, conn)
        AccDAO->>DB: UPDATE accounts SET balance = ?
        
        Service->>TxnDAO: create(txn, conn)
        TxnDAO->>DB: INSERT INTO transactions (...)
        
        Service->>DB: conn.commit()
        
        Service-->>Kiosk: Return Generated Receipt DTO
        Kiosk->>Customer: Dispense Currency & Display Thermal Receipt
    end
```

---

## 2. Inter-Account Atomic Money Transfer Sequence

```mermaid
sequenceDiagram
    autonumber
    actor Customer as 👤 Customer
    participant Servlet as 🖥️ TransferServlet
    participant Service as ⚙️ ATMServiceImpl
    participant AccDAO as 🗄️ AccountDAOImpl
    participant TxnDAO as 📝 TransactionDAOImpl
    participant DB as 💾 MySQL Database

    Customer->>Servlet: POST targetAccount='1001008963', amount=4500
    Servlet->>Service: transfer(sourceCard, targetAccount, 4500, note)
    
    Service->>AccDAO: findByAccountNumber('1001008963')
    AccDAO-->>Service: Target Account Record (Status: ACTIVE)
    
    Service->>DB: conn.setAutoCommit(false) [BEGIN TRANSACTION]
    
    critical Atomic Multi-Account Settlement
        Service->>AccDAO: updateBalance(sourceId, senderNewBal, conn)
        AccDAO->>DB: UPDATE accounts (Debit Sender)
        
        Service->>AccDAO: updateBalance(targetId, recipientNewBal, conn)
        AccDAO->>DB: UPDATE accounts (Credit Recipient)
        
        Service->>TxnDAO: create(debitTxn, conn)
        TxnDAO->>DB: INSERT INTO transactions (TRANSFER_OUT)
        
        Service->>TxnDAO: create(creditTxn, conn)
        TxnDAO->>DB: INSERT INTO transactions (TRANSFER_IN)
        
        Service->>DB: conn.commit() [COMMITTED ATOMICALLY]
    option On Any SQL Failure
        Service->>DB: conn.rollback() [SAFE ROLLBACK TO PRIOR BALANCES]
    end

    Service-->>Servlet: Return Transfer Receipt Advice
    Servlet->>Customer: Display Thermal Receipt Slip & PDF Download Button
```
