# Production Dockerfile for Apex Bank Management System
FROM tomcat:9.0-jdk17-temurin

# Clean default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Replace Tomcat server.xml (disables shutdown port and opens 8080 & 10000)
COPY server.xml /usr/local/tomcat/conf/server.xml

# Deploy ROOT.war and extract directly into ROOT webapp directory
COPY ROOT.war /usr/local/tomcat/webapps/ROOT.war
RUN mkdir -p /usr/local/tomcat/webapps/ROOT && \
    cd /usr/local/tomcat/webapps/ROOT && \
    jar -xf /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080 10000

CMD ["catalina.sh", "run"]
