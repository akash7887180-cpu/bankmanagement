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
                System.out.println("[ApexBank] Database tables already present and ready.");
            }
        } catch (Exception e) {
            System.err.println("[ApexBank] Database initialization check: " + e.getMessage());
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
