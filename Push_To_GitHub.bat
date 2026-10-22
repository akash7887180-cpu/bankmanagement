@echo off
title Uploading Apex Bank Management to GitHub...
cd /d "C:\Users\Akash\Downloads\bankmanagement"
color 0A

echo ======================================================================
echo    APEX BANK MANAGEMENT SYSTEM - GITHUB UPLOADER
echo ======================================================================
echo.
echo Connecting to: https://github.com/akash7887180-cpu/bankmanagement.git
echo.
echo If a browser window or login prompt pops up:
echo 1. Click "Sign in with your browser"
echo 2. Click "Authorize GitCredentialManager"
echo.
echo Uploading files now... Please wait a few seconds...
echo.

git push -u origin main

echo.
if %ERRORLEVEL% EQU 0 (
    echo ======================================================================
    echo  SUCCESS! ALL FILES HAVE BEEN UPLOADED TO GITHUB!
    echo  View your code at: https://github.com/akash7887180-cpu/bankmanagement
    echo ======================================================================
) else (
    echo ======================================================================
    echo  Upload failed or was cancelled. 
    echo  If needed, make sure you are signed into GitHub in your browser.
    echo ======================================================================
)
echo.
pause
