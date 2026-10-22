<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Apex National Bank - ATM & Internet Banking</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.1/font/bootstrap-icons.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css">
</head>
<body>

    <!-- Header Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark navbar-bank">
        <div class="container">
            <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="36" height="36" class="me-2">
                <span>APEX NATIONAL BANK</span>
                <span class="brand-badge">SECURE CORE</span>
            </a>
            <div class="d-flex align-items-center gap-2">
                <button class="theme-btn" id="themeToggleBtn" title="Toggle Dark/Light Mode">
                    <i class="bi bi-moon-stars-fill" id="themeIcon"></i>
                </button>
                <a href="${pageContext.request.contextPath}/admin/login" class="btn btn-outline-light btn-sm">
                    <i class="bi bi-shield-lock me-1"></i> Admin Portal
                </a>
            </div>
        </div>
    </nav>

    <!-- Hero Section -->
    <div class="container py-5 my-auto">
        <div class="row align-items-center g-5">
            <div class="col-lg-7">
                <div class="badge bg-primary-subtle text-primary border border-primary px-3 py-2 rounded-pill mb-3">
                    <i class="bi bi-award me-1"></i> Final Year Engineering Capstone Project • Java OOP Architecture
                </div>
                <h1 class="display-4 fw-bold lh-1 mb-3">
                    Enterprise ATM &amp; Internet Banking System
                </h1>
                <p class="lead text-secondary mb-4">
                    Experience an authentic ATM hardware simulator and modern internet banking platform built purely with 
                    <strong>Java Servlets, JSP, JDBC, and pure OOP principles</strong>. Fully featured with ACID-compliant transfers, 
                    dynamic thermal receipts, PIN lockout security, and an executive administration suite.
                </p>

                <div class="d-flex flex-wrap gap-3 mb-5">
                    <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg px-4 shadow-sm">
                        <i class="bi bi-credit-card-2-front me-2"></i> Insert ATM Card (Web Mode)
                    </a>
                    <a href="${pageContext.request.contextPath}/atm/kiosk" class="btn btn-dark btn-lg px-4 shadow-sm border-secondary">
                        <i class="bi bi-terminal me-2"></i> Launch ATM Kiosk Simulator
                    </a>
                </div>

                <!-- Feature Badges -->
                <div class="row g-3 pt-3 border-top">
                    <div class="col-sm-4">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-check-circle-fill text-success fs-5 me-2"></i>
                            <div>
                                <small class="fw-bold d-block">Pure Java OOP</small>
                                <span class="text-muted" style="font-size: 0.8rem;">No frameworks used</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-file-earmark-pdf-fill text-danger fs-5 me-2"></i>
                            <div>
                                <small class="fw-bold d-block">Thermal Receipts</small>
                                <span class="text-muted" style="font-size: 0.8rem;">PDF & Print Support</span>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-4">
                        <div class="d-flex align-items-center">
                            <i class="bi bi-shield-check text-primary fs-5 me-2"></i>
                            <div>
                                <small class="fw-bold d-block">Enterprise Security</small>
                                <span class="text-muted" style="font-size: 0.8rem;">3-Attempt Card Lock</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Demo Credentials Card -->
            <div class="col-lg-5">
                <div class="card card-bank p-4">
                    <div class="d-flex align-items-center justify-content-between mb-3 pb-2 border-bottom">
                        <h5 class="fw-bold mb-0"><i class="bi bi-key-fill text-warning me-2"></i>Demo Test Accounts</h5>
                        <span class="badge bg-success">Ready to Test</span>
                    </div>

                    <div class="list-group list-group-flush mb-4">
                        <div class="list-group-item bg-transparent px-0 border-primary">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong class="text-success">Manoj S</strong> <span class="badge bg-success-subtle text-success">Newly Enrolled</span>
                                    <div class="text-muted small">Card: <code>4532 7788 9900 1122</code></div>
                                </div>
                                <span class="badge bg-success">PIN: 1234</span>
                            </div>
                        </div>

                        <div class="list-group-item bg-transparent px-0 border-primary">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong class="text-primary">Akash M</strong> <span class="badge bg-primary-subtle text-primary">Active Card</span>
                                    <div class="text-muted small">Card: <code>4532 9988 1122 3344</code></div>
                                </div>
                                <span class="badge bg-primary">PIN: 1234</span>
                            </div>
                        </div>

                        <div class="list-group-item bg-transparent px-0">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong class="text-primary">John Doe</strong> (Savings Account)
                                    <div class="text-muted small">Card: <code>4532 7890 1234 5678</code></div>
                                </div>
                                <span class="badge bg-secondary">PIN: 1234</span>
                            </div>
                        </div>

                        <div class="list-group-item bg-transparent px-0">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong class="text-primary">Priya Sharma</strong> (Current Account)
                                    <div class="text-muted small">Card: <code>5241 8901 2345 6789</code></div>
                                </div>
                                <span class="badge bg-secondary">PIN: 1234</span>
                            </div>
                        </div>

                        <div class="list-group-item bg-transparent px-0">
                            <div class="d-flex justify-content-between align-items-center">
                                <div>
                                    <strong class="text-primary">Super Administrator</strong>
                                    <div class="text-muted small">Username: <code>admin</code></div>
                                </div>
                                <span class="badge bg-dark">Pass: admin123</span>
                            </div>
                        </div>
                    </div>

                    <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-primary w-100">
                        <i class="bi bi-arrow-right-circle me-1"></i> Proceed to Login
                    </a>
                </div>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <footer>
        <div class="container text-center">
            <p class="mb-1 fw-semibold text-light">Apex National Banking Corporation &copy; 2026. All rights reserved.</p>
            <p class="small text-muted mb-0">Engineered with Java Servlets, JSP, JDBC, MySQL, and Object-Oriented Software Design Patterns.</p>
        </div>
    </footer>

    <!-- Bootstrap 5 JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
</body>
</html>
