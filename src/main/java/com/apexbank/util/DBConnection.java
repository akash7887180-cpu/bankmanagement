package com.apexbank.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Singleton Pattern implementation for Database Connection management.
 * Demonstrates Singleton Design Pattern, Encapsulation, and JDBC Connectivity.
 */
public class DBConnection {

    private static DBConnection instance;
    private final String url;
    private final String username;
    private final String password;

    private DBConnection() {
        String driver = ConfigReader.get("db.driver", "com.mysql.cj.jdbc.Driver");
        this.url = ConfigReader.get("db.url", "jdbc:mysql://localhost:3306/apex_bank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        this.username = ConfigReader.get("db.username", "root");
        this.password = ConfigReader.get("db.password", "root");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.err.println("Fatal: MySQL JDBC Driver not found in classpath: " + e.getMessage());
        }
    }

    /**
     * Thread-safe Singleton instance retrieval using double-checked locking.
     */
    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    /**
     * Opens a new database connection from configured parameters.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.username, this.password);
    }

    /**
     * Safe resource cleanup helper method.
     */
    public static void close(AutoCloseable... resources) {
        if (resources == null) return;
        for (AutoCloseable res : resources) {
            if (res != null) {
                try {
                    res.close();
                } catch (Exception ignored) {
                }
            }
        }
    }
}
