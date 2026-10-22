<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty pageTitle ? pageTitle : 'Apex National Bank - ATM & Internet Banking'}</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <!-- Custom Application CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
    <c:if test="${isReceiptPage}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/receipt.css">
    </c:if>
    <c:if test="${isKioskPage}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/atm-kiosk.css">
    </c:if>
</head>
<body>
