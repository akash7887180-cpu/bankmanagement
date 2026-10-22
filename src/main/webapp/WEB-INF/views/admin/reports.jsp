<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Financial Reports - Apex Admin" />
<jsp:include page="../common/header.jsp" />

<!-- Admin Top Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary px-3">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="28" height="28" class="me-2">
            <span>APEX BANK ADMIN</span>
            <span class="badge bg-danger ms-2">REPORTS</span>
        </a>
        <div class="d-flex align-items-center gap-2">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-light btn-sm">
                <i class="bi bi-speedometer2 me-1"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn-outline-danger btn-sm">
                Logout
            </a>
        </div>
    </div>
</nav>

<div class="container-fluid px-4 py-4">
    <jsp:include page="../common/messages.jsp" />

    <div class="card card-bank p-4 mb-4">
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-3 border-bottom">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-file-earmark-bar-graph text-primary me-2"></i>Executive Financial Report</h4>
                <small class="text-secondary">Comprehensive financial statement for period: <strong><c:out value="${reportPeriod}" /></strong></small>
            </div>
            <div>
                <button type="button" class="btn btn-primary" onclick="window.print()">
                    <i class="bi bi-printer me-1"></i> Print Formal Report
                </button>
            </div>
        </div>

        <!-- Key Financial Summary Table -->
        <div class="row g-4 mb-5">
            <div class="col-md-6">
                <div class="p-4 border rounded-3 bg-light">
                    <h5 class="fw-bold text-dark mb-3">Balance Sheet Overview</h5>
                    <table class="table table-sm">
                        <tbody>
                            <tr>
                                <td class="text-secondary">Total Customers Enrolled:</td>
                                <td class="text-end fw-bold">${stats.totalCustomers}</td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Total Active Accounts:</td>
                                <td class="text-end fw-bold">${stats.totalAccounts}</td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Total ATM Cards Issued:</td>
                                <td class="text-end fw-bold">${stats.totalCards}</td>
                            </tr>
                            <tr class="table-primary fw-bold">
                                <td>Aggregate Bank Balance (Assets):</td>
                                <td class="text-end fs-6 text-primary"><c:out value="${formattedTotalBalance}" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <div class="col-md-6">
                <div class="p-4 border rounded-3 bg-light">
                    <h5 class="fw-bold text-dark mb-3">Volume &amp; Liquidity Flow</h5>
                    <table class="table table-sm">
                        <tbody>
                            <tr>
                                <td class="text-secondary">Cumulative Inward Deposits:</td>
                                <td class="text-end fw-bold text-success"><c:out value="${formattedDeposits}" /></td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Cumulative Outward Withdrawals:</td>
                                <td class="text-end fw-bold text-danger"><c:out value="${formattedWithdrawals}" /></td>
                            </tr>
                            <tr>
                                <td class="text-secondary">Today's Total Transactions:</td>
                                <td class="text-end fw-bold">${stats.todayTxnCount} Transactions</td>
                            </tr>
                            <tr class="table-warning fw-bold">
                                <td>Today's Gross Turnover Volume:</td>
                                <td class="text-end fs-6 text-dark"><c:out value="${formattedTodayVolume}" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="p-3 bg-body-tertiary rounded-3 text-center text-muted small">
            Report generated on <strong><c:out value="${generatedDate}" /></strong> • Apex National Banking Corporation Core Banking System
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
