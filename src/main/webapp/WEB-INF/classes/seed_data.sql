-- =============================================================================
-- Apex National Banking Corporation - Sample Seed Data
-- =============================================================================
USE apex_bank_db;

-- -----------------------------------------------------------------------------
-- ADMIN SEED DATA
-- Default Admin credentials:
-- Username: admin
-- Password: admin123
-- SHA-256 of "admin123": 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- -----------------------------------------------------------------------------
INSERT INTO admins (admin_id, username, password_hash, full_name, email, role, is_active)
VALUES (1, 'admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Vikram Malhotra', 'admin@apexbank.com', 'SUPER_ADMIN', TRUE);

INSERT INTO admins (admin_id, username, password_hash, full_name, email, role, is_active)
VALUES (2, 'manager', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'Anita Desai', 'manager@apexbank.com', 'BANK_MANAGER', TRUE);

-- -----------------------------------------------------------------------------
-- CUSTOMERS SEED DATA
-- -----------------------------------------------------------------------------
INSERT INTO customers (customer_id, first_name, last_name, email, phone, dob, gender, address, city, state, pincode, id_proof_type, id_proof_number)
VALUES 
(1, 'John', 'Doe', 'john.doe@example.com', '9876543210', '1990-05-15', 'MALE', 'Flat 402, Skyline Towers, Sector 18', 'Gurugram', 'Haryana', '122002', 'AADHAAR', '4521-8974-1234'),
(2, 'Priya', 'Sharma', 'priya.sharma@example.com', '9811223344', '1994-08-22', 'FEMALE', 'B-12/4, Green Glen Layout, Bellandur', 'Bengaluru', 'Karnataka', '560103', 'PAN', 'ABCPS8921K'),
(3, 'Rahul', 'Verma', 'rahul.verma@example.com', '9988776655', '1988-12-03', 'MALE', '104, Marina View Apartments, Marine Lines', 'Mumbai', 'Maharashtra', '400020', 'PASSPORT', 'M8472910'),
(4, 'Ananya', 'Iyer', 'ananya.iyer@example.com', '9712345678', '1996-03-30', 'FEMALE', '45, Cathedral Road, Gopalapuram', 'Chennai', 'Tamil Nadu', '600086', 'AADHAAR', '7890-1234-5678');

-- -----------------------------------------------------------------------------
-- ACCOUNTS SEED DATA
-- Account 1: Savings (John Doe) - ₹58,450.00
-- Account 2: Current (Priya Sharma) - ₹1,25,000.00 (Overdraft limit ₹25,000)
-- Account 3: Savings (Rahul Verma) - ₹12,800.00
-- Account 4: Savings (Ananya Iyer) - ₹95,200.00
-- -----------------------------------------------------------------------------
INSERT INTO accounts (account_id, account_number, customer_id, account_type, balance, minimum_balance, interest_rate, overdraft_limit, status)
VALUES 
(1, '1001004521', 1, 'SAVINGS', 58450.00, 1000.00, 4.00, 0.00, 'ACTIVE'),
(2, '1001008963', 2, 'CURRENT', 125000.00, 5000.00, 0.00, 25000.00, 'ACTIVE'),
(3, '1001007741', 3, 'SAVINGS', 12800.00, 1000.00, 4.00, 0.00, 'ACTIVE'),
(4, '1001003322', 4, 'SAVINGS', 95200.00, 1000.00, 4.00, 0.00, 'ACTIVE');

-- -----------------------------------------------------------------------------
-- ATM CARDS SEED DATA
-- Default PIN for all cards: 1234
-- SHA-256 of "1234": 03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4
-- -----------------------------------------------------------------------------
INSERT INTO atm_cards (card_id, card_number, account_id, customer_id, card_type, pin_hash, pin_salt, expiry_date, cvv_hash, daily_limit, failed_pin_attempts, status)
VALUES 
(1, '4532789012345678', 1, 1, 'DEBIT_RUPAY', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'salt123', '2029-12-31', '321', 40000.00, 0, 'ACTIVE'),
(2, '5241890123456789', 2, 2, 'DEBIT_VISA', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'salt123', '2028-10-31', '654', 50000.00, 0, 'ACTIVE'),
(3, '6071890123456789', 3, 3, 'DEBIT_MASTERCARD', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'salt123', '2027-06-30', '987', 40000.00, 0, 'ACTIVE'),
(4, '4111222233334444', 4, 4, 'DEBIT_RUPAY', '03ac674216f3e15c761ee1a5e255f067953623c8b388b4459e13f978d7c846f4', 'salt123', '2030-01-31', '147', 40000.00, 0, 'ACTIVE');

-- -----------------------------------------------------------------------------
-- HISTORICAL TRANSACTIONS SEED DATA
-- -----------------------------------------------------------------------------
INSERT INTO transactions (transaction_id, reference_number, account_id, card_id, transaction_type, amount, balance_after, destination_account_id, description, channel, atm_terminal_id, status, created_at)
VALUES 
(1, 'TXN-20261015-00101', 1, 1, 'DEPOSIT', 25000.00, 50000.00, NULL, 'ATM Cash Deposit', 'ATM', 'APEX-ATM-042', 'SUCCESS', '2026-10-15 10:15:30'),
(2, 'TXN-20261016-00102', 1, 1, 'WITHDRAWAL', 2000.00, 48000.00, NULL, 'ATM Cash Withdrawal', 'ATM', 'APEX-ATM-042', 'SUCCESS', '2026-10-16 14:22:10'),
(3, 'TXN-20261018-00103', 1, 1, 'FAST_CASH', 5000.00, 43000.00, NULL, 'ATM Fast Cash Dispense', 'ATM', 'APEX-ATM-042', 'SUCCESS', '2026-10-18 18:40:05'),
(4, 'TXN-20261020-00104', 1, 1, 'TRANSFER_OUT', 4550.00, 38450.00, 2, 'Fund Transfer to 1001008963', 'ATM', 'APEX-ATM-042', 'SUCCESS', '2026-10-20 11:05:44'),
(5, 'TXN-20261020-00105', 2, 2, 'TRANSFER_IN', 4550.00, 104550.00, 1, 'Fund Received from 1001004521', 'ATM', 'APEX-ATM-042', 'SUCCESS', '2026-10-20 11:05:44'),
(6, 'TXN-20261021-00106', 1, 1, 'DEPOSIT', 20000.00, 58450.00, NULL, 'ATM Cash Deposit', 'ATM', 'APEX-ATM-042', 'SUCCESS', '2026-10-21 16:30:12');

-- -----------------------------------------------------------------------------
-- SAMPLE RECEIPT SEED DATA
-- -----------------------------------------------------------------------------
INSERT INTO receipt_history (receipt_id, receipt_number, transaction_id, atm_id, terminal_id, branch_name, masked_card, masked_account, customer_name, amount, available_balance)
VALUES
(1, 'RCPT-20261021-99881', 6, 'APEX-ATM-042', 'TERM-DEL-08', 'Connaught Place Financial Center, New Delhi', '4532-XXXX-XXXX-5678', '100100XXXX', 'John Doe', 20000.00, 58450.00);

-- -----------------------------------------------------------------------------
-- AUDIT LOGS SEED DATA
-- -----------------------------------------------------------------------------
INSERT INTO audit_logs (log_id, action, performed_by, details, ip_address)
VALUES
(1, 'SYSTEM_INIT', 'SYSTEM', 'Database initialized with seed customers, accounts, and cards.', '127.0.0.1'),
(2, 'ADMIN_LOGIN', 'admin', 'Super Admin logged in successfully.', '127.0.0.1');
