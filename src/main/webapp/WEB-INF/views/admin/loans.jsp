<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Loan Management - Admin Console" />
<jsp:include page="../common/header.jsp" />

<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary sticky-top">
    <div class="container">
        <a class="navbar-brand fw-bold text-danger d-flex align-items-center" href="${pageContext.request.contextPath}/admin/dashboard">
            <i class="bi bi-shield-lock-fill me-2"></i>APEX ADMIN CONSOLE
        </a>
        <div class="d-flex align-items-center gap-2">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-sm btn-outline-light">
                <i class="bi bi-arrow-left me-1"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/tickets" class="btn btn-sm btn-outline-warning">
                <i class="bi bi-life-preserver me-1"></i> Support Tickets
            </a>
            <a href="${pageContext.request.contextPath}/admin/login?action=logout" class="btn btn-sm btn-danger">
                <i class="bi bi-box-arrow-right me-1"></i> Logout
            </a>
        </div>
    </div>
</nav>

<div class="container py-4">
    <jsp:include page="../common/messages.jsp" />

    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h3 class="fw-bold mb-0">
                <i class="bi bi-bank2 text-warning me-2"></i>Loan &amp; Gold Pledge Management
            </h3>
            <small class="text-secondary">Review credit underwriting, gold appraisals, and disburse approved loan funds</small>
        </div>
        <div class="mt-2 mt-md-0">
            <span class="badge bg-primary px-3 py-2 fs-6">
                Total Loans: ${empty loans ? 0 : loans.size()}
            </span>
        </div>
    </div>

    <!-- Loan Applications Table -->
    <div class="card card-bank p-4">
        <div class="table-responsive">
            <table class="table table-bank mb-0 align-middle">
                <thead>
                    <tr>
                        <th>Loan Reference</th>
                        <th>Customer / Account</th>
                        <th>Category &amp; Type</th>
                        <th class="text-end">Amount</th>
                        <th class="text-end">Monthly EMI</th>
                        <th>Gold Specs</th>
                        <th>Status</th>
                        <th>Applied</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty loans}">
                            <c:forEach var="l" items="${loans}">
                                <tr>
                                    <td>
                                        <code class="fw-bold text-primary"><c:out value="${l.loanReference}" /></code>
                                    </td>
                                    <td>
                                        <strong><c:out value="${l.customerName}" /></strong><br>
                                        <small class="text-muted">Acc: <code><c:out value="${l.accountNumber}" /></code></small>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${l.loanCategory == 'GOLD_LOAN'}">
                                                <span class="badge bg-warning text-dark"><i class="bi bi-gem me-1"></i>Gold Loan</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-primary"><i class="bi bi-cash-coin me-1"></i>Money Loan</span>
                                            </c:otherwise>
                                        </c:choose>
                                        <div class="small text-muted"><c:out value="${l.loanType}" /></div>
                                    </td>
                                    <td class="text-end fw-bold text-light"><c:out value="${l.formattedAmount}" /></td>
                                    <td class="text-end fw-bold text-success"><c:out value="${l.formattedEmi}" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${l.loanCategory == 'GOLD_LOAN'}">
                                                <small class="text-warning">
                                                    <strong>${l.goldWeightGrams}g</strong> @ ${l.goldKarat}K<br>
                                                    <span class="text-muted">${l.ornamentDetails}</span>
                                                </small>
                                            </c:when>
                                            <c:otherwise>
                                                <small class="text-muted">Tenure: ${l.tenureMonths}m @ ${l.interestRate}%</small>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${l.status == 'PENDING'}">
                                                <span class="badge bg-warning text-dark">Pending</span>
                                            </c:when>
                                            <c:when test="${l.status == 'APPROVED'}">
                                                <span class="badge bg-info">Approved</span>
                                            </c:when>
                                            <c:when test="${l.status == 'DISBURSED'}">
                                                <span class="badge bg-success">Disbursed</span>
                                            </c:when>
                                            <c:when test="${l.status == 'REJECTED'}">
                                                <span class="badge bg-danger">Rejected</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary"><c:out value="${l.status}" /></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><small><fmt:formatDate value="${l.appliedDate}" pattern="dd-MMM HH:mm" /></small></td>
                                    <td class="text-center">
                                        <div class="d-flex justify-content-center gap-1">
                                            <c:if test="${l.status == 'PENDING'}">
                                                <!-- Approve Button -->
                                                <form action="${pageContext.request.contextPath}/admin/loans" method="POST" style="display:inline;">
                                                    <input type="hidden" name="action" value="approve">
                                                    <input type="hidden" name="loanId" value="${l.loanId}">
                                                    <input type="hidden" name="adminRemarks" value="Approved by Bank Manager.">
                                                    <button type="submit" class="btn btn-sm btn-outline-info" title="Approve Loan">
                                                        <i class="bi bi-check-lg"></i>
                                                    </button>
                                                </form>
                                                <!-- Reject Button -->
                                                <form action="${pageContext.request.contextPath}/admin/loans" method="POST" style="display:inline;" onsubmit="return confirm('Reject this loan application?');">
                                                    <input type="hidden" name="action" value="reject">
                                                    <input type="hidden" name="loanId" value="${l.loanId}">
                                                    <input type="hidden" name="adminRemarks" value="Application rejected as per underwriting guidelines.">
                                                    <button type="submit" class="btn btn-sm btn-outline-danger" title="Reject Loan">
                                                        <i class="bi bi-x-lg"></i>
                                                    </button>
                                                </form>
                                            </c:if>

                                            <c:if test="${l.status == 'APPROVED' || l.status == 'PENDING'}">
                                                <!-- Disburse Button -->
                                                <form action="${pageContext.request.contextPath}/admin/loans" method="POST" style="display:inline;" onsubmit="return confirm('DISBURSE LOAN FUNDS? This will directly credit ${l.formattedAmount} into customer account ${l.accountNumber}.');">
                                                    <input type="hidden" name="action" value="disburse">
                                                    <input type="hidden" name="loanId" value="${l.loanId}">
                                                    <input type="hidden" name="adminRemarks" value="Funds disbursed directly into customer bank balance.">
                                                    <button type="submit" class="btn btn-sm btn-success fw-bold" title="Disburse Funds Now">
                                                        <i class="bi bi-cash-stack me-1"></i> Disburse
                                                    </button>
                                                </form>
                                            </c:if>

                                            <c:if test="${l.status == 'DISBURSED'}">
                                                <span class="text-success small"><i class="bi bi-check2-all me-1"></i>Credited</span>
                                            </c:if>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" class="text-center py-5 text-muted">
                                    <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                    No loan applications received yet.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
