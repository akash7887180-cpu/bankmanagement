<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Helpdesk Tickets - Admin Console" />
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
            <a href="${pageContext.request.contextPath}/admin/loans" class="btn btn-sm btn-outline-warning">
                <i class="bi bi-bank me-1"></i> Loan Console
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
                <i class="bi bi-life-preserver text-danger me-2"></i>Customer Grievance &amp; Problem Solving Desk
            </h3>
            <small class="text-secondary">Investigate ATM failed dispense disputes, card emergency locks, and banking complaints</small>
        </div>
        <div class="mt-2 mt-md-0">
            <span class="badge bg-danger px-3 py-2 fs-6">
                Total Tickets: ${empty tickets ? 0 : tickets.size()}
            </span>
        </div>
    </div>

    <!-- Tickets Table -->
    <div class="card card-bank p-4">
        <div class="table-responsive">
            <table class="table table-bank mb-0 align-middle">
                <thead>
                    <tr>
                        <th>Ticket Number</th>
                        <th>Customer / Contact</th>
                        <th>Category</th>
                        <th>Priority</th>
                        <th>Subject &amp; Description</th>
                        <th>Disputed Amt</th>
                        <th>Status</th>
                        <th>Created</th>
                        <th class="text-center">Resolve</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty tickets}">
                            <c:forEach var="t" items="${tickets}">
                                <tr>
                                    <td>
                                        <code class="fw-bold text-primary"><c:out value="${t.ticketNumber}" /></code>
                                    </td>
                                    <td>
                                        <strong><c:out value="${t.customerName}" /></strong><br>
                                        <small class="text-muted"><i class="bi bi-telephone me-1"></i>${t.customerPhone}</small><br>
                                        <small class="text-muted">Acc: <code>${t.accountNumber}</code></small>
                                    </td>
                                    <td>
                                        <span class="badge bg-dark border border-secondary">
                                            <c:out value="${t.issueType.displayName}" />
                                        </span>
                                        <c:if test="${not empty t.transactionReference}">
                                            <div class="mt-1 small text-muted">TXN: <code>${t.transactionReference}</code></div>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.priority == 'EMERGENCY'}">
                                                <span class="badge bg-danger"><i class="bi bi-exclamation-octagon-fill me-1"></i>Emergency</span>
                                            </c:when>
                                            <c:when test="${t.priority == 'HIGH'}">
                                                <span class="badge bg-warning text-dark"><i class="bi bi-exclamation-triangle-fill me-1"></i>High</span>
                                            </c:when>
                                            <c:when test="${t.priority == 'MEDIUM'}">
                                                <span class="badge bg-info">Medium</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary">Low</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <strong><c:out value="${t.subject}" /></strong>
                                        <p class="mb-1 text-secondary small" style="max-width: 280px;"><c:out value="${t.description}" /></p>
                                        <c:if test="${not empty t.resolutionRemarks}">
                                            <div class="p-1 px-2 rounded bg-dark border border-success small text-success">
                                                <strong>Resolution:</strong> <c:out value="${t.resolutionRemarks}" />
                                                <span class="text-muted">(${t.resolvedBy})</span>
                                            </div>
                                        </c:if>
                                    </td>
                                    <td class="fw-bold text-light">${t.formattedDisputedAmount}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.status == 'OPEN'}">
                                                <span class="badge bg-warning text-dark"><i class="bi bi-envelope-open me-1"></i>Open</span>
                                            </c:when>
                                            <c:when test="${t.status == 'IN_PROGRESS'}">
                                                <span class="badge bg-info"><i class="bi bi-gear-fill me-1"></i>In Progress</span>
                                            </c:when>
                                            <c:when test="${t.status == 'RESOLVED'}">
                                                <span class="badge bg-success"><i class="bi bi-check-all me-1"></i>Resolved</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge bg-secondary"><c:out value="${t.status}" /></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><small><fmt:formatDate value="${t.createdAt}" pattern="dd-MMM HH:mm" /></small></td>
                                    <td class="text-center">
                                        <c:if test="${t.status != 'RESOLVED' && t.status != 'CLOSED'}">
                                            <button type="button" class="btn btn-sm btn-primary fw-bold" 
                                                    data-bs-toggle="modal" data-bs-target="#resolveModal${t.ticketId}">
                                                <i class="bi bi-check2-circle me-1"></i> Resolve
                                            </button>

                                            <!-- Resolve Modal for Ticket -->
                                            <div class="modal fade" id="resolveModal${t.ticketId}" tabindex="-1" aria-hidden="true">
                                                <div class="modal-dialog modal-dialog-centered text-start">
                                                    <div class="modal-content card-bank border-primary">
                                                        <div class="modal-header border-secondary">
                                                            <h5 class="modal-title fw-bold">Resolve Ticket: ${t.ticketNumber}</h5>
                                                            <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal"></button>
                                                        </div>
                                                        <form action="${pageContext.request.contextPath}/admin/tickets" method="POST">
                                                            <input type="hidden" name="ticketId" value="${t.ticketId}">
                                                            <div class="modal-body">
                                                                <div class="mb-3">
                                                                    <label class="form-label fw-semibold">New Ticket Status</label>
                                                                    <select class="form-select" name="status">
                                                                        <option value="RESOLVED" selected>RESOLVED (Issue Investigated &amp; Solved)</option>
                                                                        <option value="IN_PROGRESS">IN_PROGRESS (Currently Under Examination)</option>
                                                                        <option value="CLOSED">CLOSED (Finalized)</option>
                                                                    </select>
                                                                </div>
                                                                <div class="mb-3">
                                                                    <label class="form-label fw-semibold">Resolution Remarks for Customer</label>
                                                                    <textarea class="form-control" name="resolutionRemarks" rows="3" placeholder="e.g. Verified ATM journal logs. ₹2,000 auto-reversed to account balance. Dispute closed." required></textarea>
                                                                </div>
                                                            </div>
                                                            <div class="modal-footer border-secondary">
                                                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                                                                <button type="submit" class="btn btn-success fw-bold">Save Resolution</button>
                                                            </div>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:if>
                                        <c:if test="${t.status == 'RESOLVED' || t.status == 'CLOSED'}">
                                            <span class="text-success small"><i class="bi bi-check-all me-1"></i>Closed</span>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" class="text-center py-5 text-muted">
                                    <i class="bi bi-check-circle fs-1 text-success d-block mb-2"></i>
                                    No customer grievances or dispute tickets pending!
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
