#!/bin/bash
set -e

echo "=== [ApexBank] Starting Container Services ==="

# Check if external DB_URL is provided, else start internal MariaDB (MySQL)
if [ -z "$DB_URL" ]; then
    echo "[ApexBank] Starting internal MariaDB server on localhost:3306..."
    service mariadb start || /etc/init.d/mariadb start

    # Wait up to 30 seconds for MySQL to be responsive
    for i in {1..30}; do
        if mysqladmin ping --silent 2>/dev/null; then
            break
        fi
        sleep 1
    done

    # Ensure apex_bank_db exists and root user has empty password locally and over TCP
    mariadb -e "CREATE DATABASE IF NOT EXISTS apex_bank_db;" || true
    mariadb -e "ALTER USER 'root'@'localhost' IDENTIFIED VIA mysql_native_password USING PASSWORD('');" 2>/dev/null || mariadb -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '';" 2>/dev/null || true
    mariadb -e "CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED VIA mysql_native_password USING PASSWORD('');" 2>/dev/null || mariadb -e "CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED BY '';" 2>/dev/null || true
    mariadb -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION; GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION; FLUSH PRIVILEGES;" || true

    # Check if customers table exists; if not, import schema & seed data
    TABLE_COUNT=$(mariadb -s -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'apex_bank_db' AND table_name = 'customers';" 2>/dev/null || echo "0")
    if [ "$TABLE_COUNT" = "0" ]; then
        echo "[ApexBank] Database is empty. Creating schema tables..."
        mariadb < /database/schema.sql
        echo "[ApexBank] Seeding demo customers (Akash, Manoj, Suresh, John, Priya, Rahul, Ananya)..."
        mariadb apex_bank_db < /database/seed_data.sql
        echo "[ApexBank] Database initialization finished successfully!"
    else
        echo "[ApexBank] Database tables already initialized."
    fi
else
    echo "[ApexBank] External database detected: $DB_URL"
fi

# Set optimized JVM memory for Render Free Tier (512MB RAM)
export JAVA_OPTS="-Xms64m -Xmx220m -XX:+UseSerialGC"

echo "[ApexBank] Starting Apache Tomcat on ports 8080 and 10000..."
exec catalina.sh run
