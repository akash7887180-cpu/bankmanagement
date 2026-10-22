@echo off
title Apex Bank - Live Internet Tunnel
cd /d "C:\Users\Akash\Downloads\bankmanagement"
color 0B

echo ======================================================================
echo    APEX BANK MANAGEMENT SYSTEM - LIVE INTERNET TUNNEL
echo ======================================================================
echo.
echo Connecting your local bank to the public internet...
echo Share the https://*.trycloudflare.com link with anyone to use the website!
echo.
.\cloudflared.exe tunnel --url http://localhost:8080
pause
