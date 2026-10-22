<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Mini Statement - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <jsp:include page="../common/messages.jsp" />

    <div class="card card-bank p-4 mb-4">
        <div class="d-flex flex-wrap justify-content-between align-items-center pb-3 mb-3 border-bottom">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-receipt text-primary me-2"></i>ATM Mini Statement</h4>
                <small class="text-secondary">Summary of the last 10 account movements</small>
            </div>
            <div class="text-end">
                <span class="text-muted small">Current Ledger Balance:</span>
                <div class="fs-4 fw-bold text-success"><c:out value="${currentBalance}" /></div>
            </div>
        </div>

        <div class="row g-2 mb-3 small text-secondary">
            <div class="col-sm-6">Account Holder: <strong class="text-dark"><c:out value="${customerName}" /></strong></div>
            <div class="col-sm-6 text-sm-end">Account Number: <strong class="font-monospace text-dark"><c:out value="${maskedAccount}" /></strong></div>
        </div>

        <div class="table-responsive">
            <table class="table table-bank mb-0">
                <thead>
                    <tr>
                        <th>Date &amp; Time</th>
                        <th>Reference</th>
                        <th>Type</th>
                        <th>Description</th>
                        <th class="text-end">Amount</th>
                        <th class="text-end">Balance After</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty transactions}">
                            <c:forEach var="t" items="${transactions}">
                                <tr>
                                    <td><c:out value="${t.formattedDate}" /></td>
                                    <td><code class="fw-bold"><c:out value="${t.referenceNumber}" /></code></td>
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
                                    <td><c:out value="${t.description}" /></td>
                                    <td class="text-end fw-bold ${t.transactionType.credit ? 'text-success' : 'text-danger'}">
                                        <c:out value="${t.transactionType.credit ? '+' : '-'}" /> ₹<c:out value="${t.amount}" />
                                    </td>
                                    <td class="text-end fw-bold">₹<c:out value="${t.balanceAfter}" /></td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="text-center py-4 text-muted">No transactions found for this account.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>

        <div class="d-flex flex-wrap gap-2 justify-content-between align-items-center mt-4 pt-3 border-top">
            <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-outline-secondary">
                <i class="bi bi-arrow-left me-1"></i> Back to Dashboard
            </a>
            <div class="d-flex gap-2">
                <button type="button" class="btn btn-outline-primary" onclick="window.print()">
                    <i class="bi bi-printer me-1"></i> Print Mini Statement
                </button>
                <a href="${pageContext.request.contextPath}/atm/statement" class="btn btn-primary">
                    <i class="bi bi-folder2-open me-1"></i> Full Statement &amp; PDF Export
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
