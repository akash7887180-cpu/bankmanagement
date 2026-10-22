# Production Dockerfile for Apex Bank Management System
FROM tomcat:9.0-jdk17-temurin

# Clean default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Deploy Apex Bank directly as the ROOT application (served at /)
COPY src/main/webapp /usr/local/tomcat/webapps/ROOT

# Expose default HTTP port
EXPOSE 8080

# Run Tomcat in foreground
CMD ["catalina.sh", "run"]
