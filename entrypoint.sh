#!/bin/bash

echo "=== [ApexBank] Starting Cloud Container Services ==="

# Check if external DB_URL is provided, else run internal MariaDB (MySQL)
if [ -z "$DB_URL" ]; then
    echo "[ApexBank] Starting internal MariaDB server on localhost:3306..."
    mkdir -p /var/run/mysqld /var/lib/mysql
    chown -R mysql:mysql /var/run/mysqld /var/lib/mysql

    # Start MariaDB daemon in background
    /usr/bin/mysqld_safe --user=mysql --skip-syslog > /tmp/mariadb.log 2>&1 &
    MARIADB_PID=$!

    # Wait up to 30 seconds for MySQL to be responsive
    for i in $(seq 1 30); do
        if mysqladmin ping --silent 2>/dev/null || mariadb-admin ping --silent 2>/dev/null; then
            echo "[ApexBank] MariaDB daemon is active and responding!"
            break
        fi
        echo "[ApexBank] Waiting for MariaDB ($i/30)..."
        sleep 1
    done

    # Ensure apex_bank_db exists and root user has empty password locally and over TCP
    mysql -e "CREATE DATABASE IF NOT EXISTS apex_bank_db;" 2>/dev/null || true
    mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED VIA mysql_native_password USING PASSWORD('');" 2>/dev/null || mysql -e "ALTER USER 'root'@'localhost' IDENTIFIED BY '';" 2>/dev/null || true
    mysql -e "CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED VIA mysql_native_password USING PASSWORD('');" 2>/dev/null || mysql -e "CREATE USER IF NOT EXISTS 'root'@'127.0.0.1' IDENTIFIED BY '';" 2>/dev/null || true
    mysql -e "GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' WITH GRANT OPTION; GRANT ALL PRIVILEGES ON *.* TO 'root'@'127.0.0.1' WITH GRANT OPTION; FLUSH PRIVILEGES;" 2>/dev/null || true

    # Check if customers table exists; if not, import schema & seed data
    TABLE_COUNT=$(mysql -s -N -e "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = 'apex_bank_db' AND table_name = 'customers';" 2>/dev/null || echo "0")
    if [ "$TABLE_COUNT" = "0" ]; then
        echo "[ApexBank] Database is empty. Creating schema tables..."
        mysql apex_bank_db < /database/schema.sql
        echo "[ApexBank] Seeding demo customers (Akash, Manoj, Suresh, John, Priya, Rahul, Ananya)..."
        mysql apex_bank_db < /database/seed_data.sql
        echo "[ApexBank] Database initialization finished successfully!"
    else
        echo "[ApexBank] Database tables already initialized."
    fi

    export DB_URL="jdbc:mysql://127.0.0.1:3306/apex_bank_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC&characterEncoding=UTF-8"
    export DB_USER="root"
    export DB_PASSWORD=""
else
    echo "[ApexBank] External database detected: $DB_URL"
fi

# Set optimized JVM memory for Render Free Tier (512MB RAM)
export JAVA_OPTS="-Xms64m -Xmx220m -XX:+UseSerialGC"

echo "[ApexBank] Starting Apache Tomcat on ports 8080 and 10000..."
exec catalina.sh run
