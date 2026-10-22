<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-expand-lg navbar-dark navbar-bank sticky-top">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/atm/dashboard">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="32" height="32" class="me-2">
            <span>APEX NATIONAL BANK</span>
            <span class="brand-badge d-none d-sm-inline">ATM NETWORK</span>
        </a>

        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#bankNavbar">
            <span class="navbar-toggler-icon"></span>
        </button>

        <div class="collapse navbar-collapse" id="bankNavbar">
            <c:if test="${not empty sessionScope.SESSION_CARD}">
                <ul class="navbar-nav me-auto ms-lg-3">
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.endsWith('dashboard') ? 'active fw-bold' : ''}" 
                           href="${pageContext.request.contextPath}/atm/dashboard">
                            <i class="bi bi-speedometer2 me-1"></i> Dashboard
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.endsWith('withdraw') ? 'active fw-bold' : ''}" 
                           href="${pageContext.request.contextPath}/atm/withdraw">
                            <i class="bi bi-cash-stack me-1"></i> Withdraw
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.endsWith('deposit') ? 'active fw-bold' : ''}" 
                           href="${pageContext.request.contextPath}/atm/deposit">
                            <i class="bi bi-wallet2 me-1"></i> Deposit
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.endsWith('fastcash') ? 'active fw-bold' : ''}" 
                           href="${pageContext.request.contextPath}/atm/fastcash">
                            <i class="bi bi-lightning-charge me-1"></i> Fast Cash
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.endsWith('transfer') ? 'active fw-bold' : ''}" 
                           href="${pageContext.request.contextPath}/atm/transfer">
                            <i class="bi bi-arrow-left-right me-1"></i> Transfer
                        </a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link ${pageContext.request.requestURI.endsWith('statement') ? 'active fw-bold' : ''}" 
                           href="${pageContext.request.contextPath}/atm/statement">
                            <i class="bi bi-clock-history me-1"></i> Statement
                        </a>
                    </li>
                </ul>

                <div class="d-flex align-items-center gap-2">
                    <a href="${pageContext.request.contextPath}/atm/kiosk" class="btn btn-sm btn-outline-info" title="Switch to Physical ATM Hardware View">
                        <i class="bi bi-display me-1"></i> Kiosk Mode
                    </a>
                    <button class="theme-btn" id="themeToggleBtn" title="Toggle Dark/Light Mode">
                        <i class="bi bi-moon-stars-fill" id="themeIcon"></i>
                    </button>
                    <a href="${pageContext.request.contextPath}/atm/logout" class="btn btn-sm btn-outline-danger" title="Eject Card and Log Out">
                        <i class="bi bi-box-arrow-right me-1"></i> Eject Card
                    </a>
                </div>
            </c:if>

            <c:if test="${empty sessionScope.SESSION_CARD and empty sessionScope.SESSION_ADMIN}">
                <div class="ms-auto d-flex align-items-center gap-2">
                    <button class="theme-btn" id="themeToggleBtn" title="Toggle Dark/Light Mode">
                        <i class="bi bi-moon-stars-fill" id="themeIcon"></i>
                    </button>
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-sm btn-primary">
                        <i class="bi bi-credit-card me-1"></i> Insert Card
                    </a>
                    <a href="${pageContext.request.contextPath}/admin/login" class="btn btn-sm btn-outline-light">
                        <i class="bi bi-shield-lock me-1"></i> Admin
                    </a>
                </div>
            </c:if>
        </div>
    </div>
</nav>
