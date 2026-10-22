# Production Dockerfile for Apex Bank Management System
FROM tomcat:9.0-jdk17-temurin

# Clean default Tomcat web applications
RUN rm -rf /usr/local/tomcat/webapps/*

# Disable shutdown port 8005 to prevent port collision with cloud health checkers
RUN sed -i 's/port="8005"/port="-1"/' /usr/local/tomcat/conf/server.xml

# Configure HTTP connector to dynamically listen on Render's $PORT (or fallback to 8080)
RUN sed -i 's/port="8080"/port="${env.PORT:8080}"/' /usr/local/tomcat/conf/server.xml

# Deploy Apex Bank directly as the ROOT application (served at /)
COPY src/main/webapp /usr/local/tomcat/webapps/ROOT

EXPOSE 8080

CMD ["catalina.sh", "run"]
