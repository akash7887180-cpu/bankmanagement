# Production Dockerfile for Apex Bank Management System (Standalone Tomcat + MariaDB)
FROM tomcat:9.0-jdk17-temurin

# Install MariaDB server (MySQL drop-in replacement) and procps
ENV DEBIAN_FRONTEND=noninteractive
RUN apt-get update && apt-get install -y --no-install-recommends \
    mariadb-server mariadb-client procps \
    && rm -rf /var/lib/apt/lists/*

# Pre-create run directories and initialize mysql data directory
RUN mkdir -p /var/run/mysqld /var/lib/mysql && \
    chown -R mysql:mysql /var/run/mysqld /var/lib/mysql && \
    mysql_install_db --user=mysql --datadir=/var/lib/mysql

# Optimize MariaDB memory for Render's 512MB RAM free tier (<60MB footprint)
RUN mkdir -p /etc/mysql/mariadb.conf.d && \
    printf "[mysqld]\nperformance_schema = OFF\ninnodb_buffer_pool_size = 32M\ninnodb_log_buffer_size = 4M\nmax_connections = 30\nbind-address = 0.0.0.0\n" > /etc/mysql/mariadb.conf.d/99-render.cnf

# Clean default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Replace Tomcat server.xml (disables shutdown port and exposes 8080 & 10000)
COPY server.xml /usr/local/tomcat/conf/server.xml

# Deploy ROOT.war and pre-extract into ROOT directory
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war
RUN mkdir -p /usr/local/tomcat/webapps/ROOT && \
    cd /usr/local/tomcat/webapps/ROOT && \
    jar -xf /usr/local/tomcat/webapps/ROOT.war

# Copy database initialization scripts
COPY database/schema.sql /database/schema.sql
COPY database/seed_data.sql /database/seed_data.sql

# Copy and set entrypoint startup script
COPY entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh && sed -i 's/\r$//' /entrypoint.sh

EXPOSE 8080 10000

ENTRYPOINT ["/entrypoint.sh"]
