-- =============================================================================
-- Apex National Banking Corporation - ATM & Core Banking Management System
-- Database Schema Definition (MySQL 5.7+ / 8.0+)
-- =============================================================================

DROP DATABASE IF EXISTS apex_bank_db;
CREATE DATABASE apex_bank_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE apex_bank_db;

-- -----------------------------------------------------------------------------
-- 1. CUSTOMERS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL UNIQUE,
    dob DATE NOT NULL,
    gender ENUM('MALE', 'FEMALE', 'OTHER') NOT NULL DEFAULT 'MALE',
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    state VARCHAR(50) NOT NULL,
    pincode VARCHAR(10) NOT NULL,
    id_proof_type VARCHAR(30) NOT NULL DEFAULT 'AADHAAR',
    id_proof_number VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_cust_phone (phone),
    INDEX idx_cust_email (email)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 2. ACCOUNTS TABLE
-- Demonstrates inheritance in database modeling: SAVINGS vs CURRENT
-- -----------------------------------------------------------------------------
CREATE TABLE accounts (
    account_id INT AUTO_INCREMENT PRIMARY KEY,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    customer_id INT NOT NULL,
    account_type ENUM('SAVINGS', 'CURRENT') NOT NULL DEFAULT 'SAVINGS',
    balance DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    minimum_balance DECIMAL(15, 2) NOT NULL DEFAULT 1000.00,
    interest_rate DECIMAL(5, 2) NOT NULL DEFAULT 4.00,
    overdraft_limit DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    status ENUM('ACTIVE', 'FROZEN', 'BLOCKED', 'CLOSED') NOT NULL DEFAULT 'ACTIVE',
    opened_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_account_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    INDEX idx_acc_num (account_number),
    INDEX idx_acc_status (status)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 3. ATM CARDS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE atm_cards (
    card_id INT AUTO_INCREMENT PRIMARY KEY,
    card_number VARCHAR(16) NOT NULL UNIQUE,
    account_id INT NOT NULL,
    customer_id INT NOT NULL,
    card_type ENUM('DEBIT_VISA', 'DEBIT_MASTERCARD', 'DEBIT_RUPAY') NOT NULL DEFAULT 'DEBIT_RUPAY',
    pin_hash VARCHAR(255) NOT NULL,
    pin_salt VARCHAR(64) NOT NULL,
    expiry_date DATE NOT NULL,
    cvv_hash VARCHAR(255) NOT NULL,
    daily_limit DECIMAL(15, 2) NOT NULL DEFAULT 40000.00,
    failed_pin_attempts INT NOT NULL DEFAULT 0,
    status ENUM('ACTIVE', 'LOCKED', 'BLOCKED', 'EXPIRED') NOT NULL DEFAULT 'ACTIVE',
    pin_plain VARCHAR(10) DEFAULT '1234',
    issued_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_card_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    CONSTRAINT fk_card_customer FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE CASCADE,
    INDEX idx_card_num (card_number),
    INDEX idx_card_status (status)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 4. TRANSACTIONS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE transactions (
    transaction_id INT AUTO_INCREMENT PRIMARY KEY,
    reference_number VARCHAR(40) NOT NULL UNIQUE,
    account_id INT NOT NULL,
    card_id INT NULL,
    transaction_type ENUM('DEPOSIT', 'WITHDRAWAL', 'FAST_CASH', 'TRANSFER_OUT', 'TRANSFER_IN', 'BALANCE_ENQUIRY', 'PIN_CHANGE') NOT NULL,
    amount DECIMAL(15, 2) NOT NULL DEFAULT 0.00,
    balance_after DECIMAL(15, 2) NOT NULL,
    destination_account_id INT NULL,
    description VARCHAR(255) NULL,
    channel ENUM('ATM', 'INTERNET_BANKING', 'BRANCH', 'FAST_CASH') NOT NULL DEFAULT 'ATM',
    atm_terminal_id VARCHAR(50) DEFAULT 'APEX-ATM-042',
    status ENUM('SUCCESS', 'FAILED', 'PENDING') NOT NULL DEFAULT 'SUCCESS',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_txn_account FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    CONSTRAINT fk_txn_card FOREIGN KEY (card_id) REFERENCES atm_cards(card_id) ON DELETE SET NULL,
    CONSTRAINT fk_txn_dest_account FOREIGN KEY (destination_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL,
    INDEX idx_txn_ref (reference_number),
    INDEX idx_txn_date (created_at),
    INDEX idx_txn_type (transaction_type)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 5. ADMINS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE admins (
    admin_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role ENUM('SUPER_ADMIN', 'BANK_MANAGER', 'AUDITOR') NOT NULL DEFAULT 'BANK_MANAGER',
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    last_login TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_admin_user (username)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 6. LOGIN HISTORY TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE login_history (
    history_id INT AUTO_INCREMENT PRIMARY KEY,
    user_type ENUM('CUSTOMER', 'ADMIN') NOT NULL,
    identifier VARCHAR(100) NOT NULL,
    ip_address VARCHAR(45) NULL,
    user_agent VARCHAR(255) NULL,
    status ENUM('SUCCESS', 'FAILED_PIN', 'ACCOUNT_LOCKED', 'FAILED_AUTH') NOT NULL,
    failure_reason VARCHAR(255) NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_hist_ident (identifier),
    INDEX idx_hist_time (timestamp)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 7. RECEIPT HISTORY TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE receipt_history (
    receipt_id INT AUTO_INCREMENT PRIMARY KEY,
    receipt_number VARCHAR(50) NOT NULL UNIQUE,
    transaction_id INT NOT NULL,
    atm_id VARCHAR(50) NOT NULL,
    terminal_id VARCHAR(50) NOT NULL,
    branch_name VARCHAR(100) NOT NULL,
    masked_card VARCHAR(25) NOT NULL,
    masked_account VARCHAR(25) NOT NULL,
    customer_name VARCHAR(100) NOT NULL,
    amount DECIMAL(15, 2) NOT NULL,
    available_balance DECIMAL(15, 2) NOT NULL,
    pdf_path VARCHAR(255) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_receipt_txn FOREIGN KEY (transaction_id) REFERENCES transactions(transaction_id) ON DELETE CASCADE,
    INDEX idx_rcpt_num (receipt_number)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 8. AUDIT LOGS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE audit_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    action VARCHAR(100) NOT NULL,
    performed_by VARCHAR(100) NOT NULL,
    details TEXT NULL,
    ip_address VARCHAR(45) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_audit_time (created_at)
) ENGINE=InnoDB;

-- -----------------------------------------------------------------------------
-- 9. OTP LOGS TABLE
-- -----------------------------------------------------------------------------
CREATE TABLE otp_logs (
    otp_id INT AUTO_INCREMENT PRIMARY KEY,
    target_phone VARCHAR(15) NOT NULL,
    target_account VARCHAR(20) NOT NULL,
    otp_code VARCHAR(6) NOT NULL,
    purpose ENUM('FORGOT_PIN', 'HIGH_VALUE_TXN', 'CARD_UNLOCK') NOT NULL,
    is_verified BOOLEAN DEFAULT FALSE,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_otp_phone (target_phone)
) ENGINE=InnoDB;
