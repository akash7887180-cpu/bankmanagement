# Production Dockerfile for Apex Bank Management System
FROM tomcat:9.0-jdk17-temurin

# Clean default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Replace Tomcat server.xml (disables shutdown port -1 and binds HTTP strictly to 8080)
COPY server.xml /usr/local/tomcat/conf/server.xml

# Deploy ROOT.war directly to webapps
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
