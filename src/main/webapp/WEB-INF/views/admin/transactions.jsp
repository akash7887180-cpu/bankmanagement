<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="System Transactions - Apex Admin" />
<jsp:include page="../common/header.jsp" />

<!-- Admin Top Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary px-3">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="28" height="28" class="me-2">
            <span>APEX BANK ADMIN</span>
            <span class="badge bg-danger ms-2">TRANSACTIONS</span>
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

    <div class="card card-bank p-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-clock-history text-primary me-2"></i>System-Wide Transaction Ledger</h4>
                <small class="text-secondary">Real-time ledger entries from all ATM terminals and internet banking channels</small>
            </div>
            <div>
                <button type="button" class="btn btn-outline-secondary btn-sm" onclick="window.print()">
                    <i class="bi bi-printer me-1"></i> Print Ledger
                </button>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-bank table-hover align-middle mb-0">
                <thead>
                    <tr>
                        <th>Txn ID</th>
                        <th>Timestamp</th>
                        <th>Reference Number</th>
                        <th>Account ID</th>
                        <th>Transaction Type</th>
                        <th>Channel / Terminal</th>
                        <th>Description</th>
                        <th class="text-end">Amount (INR)</th>
                        <th class="text-end">Balance After</th>
                        <th class="text-center">Status</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="t" items="${transactions}">
                        <tr>
                            <td>#<c:out value="${t.transactionId}" /></td>
                            <td><small><c:out value="${t.formattedDate}" /></small></td>
                            <td><code><c:out value="${t.referenceNumber}" /></code></td>
                            <td>Acc #<c:out value="${t.accountId}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${t.transactionType.credit}">
                                        <span class="badge-credit"><c:out value="${t.transactionType.description}" /></span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge-debit"><c:out value="${t.transactionType.description}" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td><small><c:out value="${t.channel}" /> • <c:out value="${t.atmTerminalId}" /></small></td>
                            <td><c:out value="${t.description}" /></td>
                            <td class="text-end fw-bold ${t.transactionType.credit ? 'text-success' : 'text-danger'}">
                                <c:out value="${t.transactionType.credit ? '+' : '-'}" /> ₹<c:out value="${t.amount}" />
                            </td>
                            <td class="text-end fw-bold">₹<c:out value="${t.balanceAfter}" /></td>
                            <td class="text-center">
                                <span class="badge bg-success"><c:out value="${t.status}" /></span>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
