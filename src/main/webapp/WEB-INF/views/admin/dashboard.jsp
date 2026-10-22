<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Admin Console - Apex National Bank" />
<jsp:include page="../common/header.jsp" />

<!-- Admin Top Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary px-3">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="28" height="28" class="me-2">
            <span>APEX BANK ADMIN</span>
            <span class="badge bg-danger ms-2"><c:out value="${admin.roleName}" /></span>
        </a>

        <div class="d-flex align-items-center gap-3">
            <span class="text-light small d-none d-md-inline">
                <i class="bi bi-person-circle me-1"></i> Logged in as: <strong><c:out value="${admin.displayName}" /></strong>
            </span>
            <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-info btn-sm">
                <i class="bi bi-display me-1"></i> Customer View
            </a>
            <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn-outline-danger btn-sm">
                <i class="bi bi-box-arrow-right me-1"></i> Logout
            </a>
        </div>
    </div>
</nav>

<!-- Admin Submenu Bar -->
<div class="bg-secondary-subtle py-2 px-3 border-bottom mb-4">
    <div class="container-fluid d-flex flex-wrap gap-2">
        <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-primary btn-sm">
            <i class="bi bi-speedometer2 me-1"></i> Overview
        </a>
        <a href="${pageContext.request.contextPath}/admin/customers" class="btn btn-outline-dark btn-sm">
            <i class="bi bi-people-fill me-1"></i> Manage Customers
        </a>
        <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-outline-dark btn-sm">
            <i class="bi bi-credit-card-2-back-fill me-1"></i> Accounts &amp; ATM Cards
        </a>
        <a href="${pageContext.request.contextPath}/admin/transactions" class="btn btn-outline-dark btn-sm">
            <i class="bi bi-clock-history me-1"></i> Transaction Log
        </a>
        <a href="${pageContext.request.contextPath}/admin/reports" class="btn btn-outline-dark btn-sm">
            <i class="bi bi-bar-chart-fill me-1"></i> Financial Reports
        </a>
    </div>
</div>

<div class="container-fluid px-4 pb-5">
    <jsp:include page="../common/messages.jsp" />

    <!-- Executive Metrics Row -->
    <div class="row g-3 mb-4">
        <div class="col-sm-6 col-xl-3">
            <div class="stat-card">
                <div class="stat-icon blue">
                    <i class="bi bi-cash-coin"></i>
                </div>
                <div>
                    <span class="text-secondary small fw-semibold">Bank Total Ledger</span>
                    <h4 class="fw-bold mb-0 text-primary"><c:out value="${formattedTotalBalance}" /></h4>
                </div>
            </div>
        </div>

        <div class="col-sm-6 col-xl-3">
            <div class="stat-card">
                <div class="stat-icon green">
                    <i class="bi bi-arrow-down-left-circle-fill"></i>
                </div>
                <div>
                    <span class="text-secondary small fw-semibold">Total Deposits</span>
                    <h4 class="fw-bold mb-0 text-success"><c:out value="${formattedDeposits}" /></h4>
                </div>
            </div>
        </div>

        <div class="col-sm-6 col-xl-3">
            <div class="stat-card">
                <div class="stat-icon orange">
                    <i class="bi bi-arrow-up-right-circle-fill"></i>
                </div>
                <div>
                    <span class="text-secondary small fw-semibold">Total Withdrawals</span>
                    <h4 class="fw-bold mb-0 text-danger"><c:out value="${formattedWithdrawals}" /></h4>
                </div>
            </div>
        </div>

        <div class="col-sm-6 col-xl-3">
            <div class="stat-card">
                <div class="stat-icon purple">
                    <i class="bi bi-people-fill"></i>
                </div>
                <div>
                    <span class="text-secondary small fw-semibold">Active Customers</span>
                    <h4 class="fw-bold mb-0"><c:out value="${stats.totalCustomers}" /> Customers</h4>
                </div>
            </div>
        </div>
    </div>

    <!-- Secondary Metrics Row -->
    <div class="row g-3 mb-4">
        <div class="col-md-4">
            <div class="card card-bank p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="text-muted small">Total Accounts Enrolled:</span>
                        <h5 class="fw-bold mb-0">${stats.totalAccounts}</h5>
                    </div>
                    <i class="bi bi-bank fs-2 text-primary opacity-50"></i>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card card-bank p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="text-muted small">Total ATM Cards Issued:</span>
                        <h5 class="fw-bold mb-0">${stats.totalCards}</h5>
                    </div>
                    <i class="bi bi-credit-card fs-2 text-warning opacity-50"></i>
                </div>
            </div>
        </div>

        <div class="col-md-4">
            <div class="card card-bank p-3">
                <div class="d-flex justify-content-between align-items-center">
                    <div>
                        <span class="text-muted small">Today's Total Txn Count:</span>
                        <h5 class="fw-bold mb-0">${stats.todayTxnCount} Transactions</h5>
                    </div>
                    <i class="bi bi-activity fs-2 text-success opacity-50"></i>
                </div>
            </div>
        </div>
    </div>

    <div class="row g-4">
        <!-- Recent System Transactions -->
        <div class="col-lg-7">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0"><i class="bi bi-clock-history text-primary me-2"></i>Recent Transactions</h5>
                    <a href="${pageContext.request.contextPath}/admin/transactions" class="btn btn-sm btn-outline-primary">
                        View All
                    </a>
                </div>

                <div class="table-responsive">
                    <table class="table table-bank table-sm mb-0">
                        <thead>
                            <tr>
                                <th>Timestamp</th>
                                <th>Ref No</th>
                                <th>Type</th>
                                <th class="text-end">Amount</th>
                                <th class="text-end">Balance After</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="t" items="${recentTransactions}">
                                <tr>
                                    <td><small><c:out value="${t.formattedDate}" /></small></td>
                                    <td><code><c:out value="${t.referenceNumber}" /></code></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.transactionType.credit}">
                                                <span class="badge-credit" style="font-size: 0.75rem;"><c:out value="${t.transactionType.description}" /></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-debit" style="font-size: 0.75rem;"><c:out value="${t.transactionType.description}" /></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td class="text-end fw-bold">₹<c:out value="${t.amount}" /></td>
                                    <td class="text-end">₹<c:out value="${t.balanceAfter}" /></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Audit Trail & Security Logs -->
        <div class="col-lg-5">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0"><i class="bi bi-shield-check text-success me-2"></i>Security Audit Trail</h5>
                    <span class="badge bg-secondary">System Logs</span>
                </div>

                <div class="list-group list-group-flush small">
                    <c:forEach var="log" items="${recentLogs}">
                        <div class="list-group-item px-0 bg-transparent">
                            <div class="d-flex justify-content-between align-items-center">
                                <strong class="text-primary"><c:out value="${log.action}" /></strong>
                                <span class="text-muted" style="font-size: 0.75rem;"><c:out value="${log.formattedDate}" /></span>
                            </div>
                            <div class="text-secondary"><c:out value="${log.details}" /></div>
                            <div class="text-muted" style="font-size: 0.75rem;">User: <c:out value="${log.performedBy}" /> • IP: <c:out value="${log.ipAddress}" /></div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
