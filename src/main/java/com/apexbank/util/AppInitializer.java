package com.apexbank.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Auto-initializes cloud MySQL database on startup if tables do not exist.
 */
@WebListener
public class AppInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("[ApexBank] Checking database initialization...");
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement()) {

            boolean tablesExist = false;
            try (ResultSet rs = stmt.executeQuery("SHOW TABLES LIKE 'customers'")) {
                if (rs.next()) {
                    tablesExist = true;
                }
            }

            if (!tablesExist) {
                System.out.println("[ApexBank] Cloud database detected without tables. Initializing schema and seed data...");
                executeSqlScript(conn, "/schema.sql");
                executeSqlScript(conn, "/seed_data.sql");
                System.out.println("[ApexBank] Database successfully initialized with tables and demo records!");
            } else {
                System.out.println("[ApexBank] Database tables already present. Ensuring loans and support_tickets tables exist...");
                ensureFeatureTablesExist(conn);
                System.out.println("[ApexBank] Database tables verified and ready.");
            }
        } catch (Throwable t) {
            System.err.println("[ApexBank] Database initialization check note: " + t.getMessage());
        }
    }

    private void ensureFeatureTablesExist(Connection conn) {
        String createLoans = "CREATE TABLE IF NOT EXISTS loans (" +
                "loan_id INT AUTO_INCREMENT PRIMARY KEY," +
                "loan_reference VARCHAR(40) NOT NULL UNIQUE," +
                "account_id INT NOT NULL," +
                "customer_id INT NOT NULL," +
                "loan_category ENUM('MONEY_LOAN', 'GOLD_LOAN') NOT NULL," +
                "loan_type VARCHAR(50) NOT NULL," +
                "amount DECIMAL(15, 2) NOT NULL," +
                "tenure_months INT NOT NULL," +
                "interest_rate DECIMAL(5, 2) NOT NULL," +
                "monthly_emi DECIMAL(15, 2) NOT NULL," +
                "purpose VARCHAR(255) NULL," +
                "monthly_income DECIMAL(15, 2) NULL," +
                "employment_type VARCHAR(50) NULL," +
                "gold_weight_grams DECIMAL(8, 2) NULL," +
                "gold_karat INT NULL," +
                "gold_market_rate DECIMAL(10, 2) NULL," +
                "ornament_details VARCHAR(255) NULL," +
                "status ENUM('PENDING', 'APPROVED', 'DISBURSED', 'REJECTED', 'CLOSED') NOT NULL DEFAULT 'PENDING'," +
                "admin_remarks TEXT NULL," +
                "applied_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "disbursed_at TIMESTAMP NULL," +
                "INDEX idx_loan_ref (loan_reference)," +
                "INDEX idx_loan_cat (loan_category)," +
                "INDEX idx_loan_status (status)" +
                ") ENGINE=InnoDB;";

        String createTickets = "CREATE TABLE IF NOT EXISTS support_tickets (" +
                "ticket_id INT AUTO_INCREMENT PRIMARY KEY," +
                "ticket_number VARCHAR(40) NOT NULL UNIQUE," +
                "customer_id INT NOT NULL," +
                "account_id INT NOT NULL," +
                "card_id INT NULL," +
                "issue_type ENUM('FAILED_ATM_CASH', 'LOST_CARD_FRAUD', 'TRANSACTION_DISPUTE', 'ACCOUNT_FROZEN', 'EMI_HARDSHIP', 'GENERAL_INQUIRY') NOT NULL," +
                "priority ENUM('LOW', 'MEDIUM', 'HIGH', 'EMERGENCY') NOT NULL DEFAULT 'MEDIUM'," +
                "subject VARCHAR(150) NOT NULL," +
                "description TEXT NOT NULL," +
                "transaction_reference VARCHAR(50) NULL," +
                "disputed_amount DECIMAL(15, 2) NULL," +
                "status ENUM('OPEN', 'IN_PROGRESS', 'RESOLVED', 'CLOSED') NOT NULL DEFAULT 'OPEN'," +
                "resolution_remarks TEXT NULL," +
                "resolved_by VARCHAR(50) NULL," +
                "resolved_at TIMESTAMP NULL," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "INDEX idx_tck_num (ticket_number)," +
                "INDEX idx_tck_status (status)," +
                "INDEX idx_tck_type (issue_type)" +
                ") ENGINE=InnoDB;";

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createLoans);
            stmt.execute(createTickets);
        } catch (Exception e) {
            System.err.println("[ApexBank] Note on ensuring feature tables: " + e.getMessage());
        }
    }

    private void executeSqlScript(Connection conn, String resourcePath) {
        try (InputStream is = AppInitializer.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("[ApexBank] Resource not found: " + resourcePath);
                return;
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.trim();
                    if (line.startsWith("--") || line.startsWith("#") || line.isEmpty()) continue;
                    sb.append(line).append(" ");
                    if (line.endsWith(";")) {
                        try (Statement stmt = conn.createStatement()) {
                            stmt.execute(sb.toString());
                        } catch (Exception ex) {
                            System.err.println("SQL exec note: " + ex.getMessage());
                        }
                        sb.setLength(0);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Failed reading " + resourcePath + ": " + e.getMessage());
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
