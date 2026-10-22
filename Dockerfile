# Production Dockerfile for Apex Bank Management System
FROM tomcat:9.0-jdk17-temurin

# Clean default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Disable shutdown port 8005 so only HTTP port 8080 is active
RUN sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml

# Deploy ROOT.war directly to webapps
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
