@echo off
title Apex ATM Banking System - Server Shutdown
echo ===================================================
echo Stopping Apex ATM Banking System
echo ===================================================
cd /d C:\apache-tomcat-9
bin\catalina.bat stop
echo Server stopped successfully.
pause
