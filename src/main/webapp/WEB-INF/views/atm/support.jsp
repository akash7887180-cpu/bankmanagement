<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Helpdesk & Problem Solving - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <jsp:include page="../common/messages.jsp" />

    <!-- Breadcrumb & Header -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <nav aria-label="breadcrumb">
                <ol class="breadcrumb mb-1 small">
                    <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/atm/dashboard">Dashboard</a></li>
                    <li class="breadcrumb-item active" aria-current="page">Customer Service &amp; Helpdesk</li>
                </ol>
            </nav>
            <h3 class="fw-bold mb-0">
                <i class="bi bi-life-preserver text-danger me-2"></i>Financial Issues &amp; Grievance Redressal
            </h3>
        </div>
        <div class="mt-2 mt-md-0">
            <span class="badge bg-danger-subtle text-danger border border-danger px-3 py-2">
                <i class="bi bi-headset me-1"></i> 24x7 Problem Solving Queue • Escalation SLA: 24h
            </span>
        </div>
    </div>

    <!-- Quick Action Emergency Cards Row -->
    <div class="row g-4 mb-4">
        <!-- Emergency Card Lock -->
        <div class="col-lg-4 col-md-6">
            <div class="card card-bank p-4 h-100 border-danger bg-gradient" style="background: linear-gradient(135deg, rgba(220, 38, 38, 0.15) 0%, rgba(15, 23, 42, 0.95) 100%);">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h5 class="fw-bold text-danger mb-0">
                        <i class="bi bi-shield-slash-fill me-2"></i>Lost / Stolen Card
                    </h5>
                    <span class="badge bg-danger">Emergency</span>
                </div>
                <p class="text-secondary small mb-3">
                    If your ATM card is misplaced, stolen, or compromised, trigger an instant security lockdown immediately.
                </p>
                <form action="${pageContext.request.contextPath}/atm/support" method="POST" onsubmit="return confirm('ARE YOU SURE? This will IMMEDIATELY LOCK your ATM card to prevent all unauthorized debits and cash withdrawals.');">
                    <input type="hidden" name="action" value="emergencyBlock">
                    <input type="hidden" name="reason" value="Customer 1-Click Emergency Lock requested via Helpdesk">
                    <button type="submit" class="btn btn-danger w-100 fw-bold shadow-sm">
                        <i class="bi bi-lock-fill me-1"></i> Emergency Lock Card Now
                    </button>
                </form>
            </div>
        </div>

        <!-- ATM Cash Dispense Failure Quick Button -->
        <div class="col-lg-4 col-md-6">
            <div class="card card-bank p-4 h-100 border-warning bg-gradient" style="background: linear-gradient(135deg, rgba(245, 158, 11, 0.15) 0%, rgba(15, 23, 42, 0.95) 100%);">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h5 class="fw-bold text-warning mb-0">
                        <i class="bi bi-cash-stack me-2"></i>Cash Not Dispensed
                    </h5>
                    <span class="badge bg-warning text-dark">High Priority</span>
                </div>
                <p class="text-secondary small mb-3">
                    Money was debited from your account balance but the ATM hardware did not deliver physical notes?
                </p>
                <button type="button" class="btn btn-warning w-100 fw-bold text-dark shadow-sm" data-bs-toggle="modal" data-bs-target="#atmDisputeModal">
                    <i class="bi bi-exclamation-triangle-fill me-1"></i> File ATM Cash Dispute
                </button>
            </div>
        </div>

        <!-- AI Assistant / Chatbot Card -->
        <div class="col-lg-4 col-md-12">
            <div class="card card-bank p-4 h-100 border-primary bg-gradient" style="background: linear-gradient(135deg, rgba(37, 99, 235, 0.15) 0%, rgba(15, 23, 42, 0.95) 100%);">
                <div class="d-flex justify-content-between align-items-center mb-2">
                    <h5 class="fw-bold text-primary mb-0">
                        <i class="bi bi-robot me-2"></i>Apex HelpBot AI
                    </h5>
                    <span class="badge bg-primary">Instant Answers</span>
                </div>
                <p class="text-secondary small mb-3">
                    Ask questions about Money Loans, Gold Loan rates, loan EMI calculation, or automated troubleshooting.
                </p>
                <a href="${pageContext.request.contextPath}/atm/chat" class="btn btn-primary w-100 fw-bold shadow-sm">
                    <i class="bi bi-chat-dots-fill me-1"></i> Launch AI Chatbot Support
                </a>
            </div>
        </div>
    </div>

    <!-- Main Section: Raise Financial Issue & My Tickets -->
    <div class="row g-4">
        <!-- Ticket Submission Form -->
        <div class="col-lg-5">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex align-items-center justify-content-between mb-3 border-bottom pb-2">
                    <h5 class="fw-bold mb-0">
                        <i class="bi bi-ticket-perforated text-primary me-2"></i>Submit Grievance Ticket
                    </h5>
                    <span class="badge bg-primary-subtle text-primary">Trackable</span>
                </div>

                <form action="${pageContext.request.contextPath}/atm/support" method="POST">
                    <div class="mb-3">
                        <label class="form-label fw-semibold">Financial Issue Category</label>
                        <select class="form-select" name="issueType" required>
                            <option value="FAILED_ATM_CASH" ${prefillType == 'FAILED_ATM_CASH' ? 'selected' : ''}>ATM Cash Not Dispensed but Debited</option>
                            <option value="LOST_CARD_FRAUD" ${prefillType == 'LOST_CARD_FRAUD' ? 'selected' : ''}>Lost / Stolen Card &amp; Emergency Lock</option>
                            <option value="TRANSACTION_DISPUTE" ${prefillType == 'TRANSACTION_DISPUTE' ? 'selected' : ''}>Unauthorized / Disputed Transaction</option>
                            <option value="ACCOUNT_FROZEN">Frozen / Blocked Account Reactivation</option>
                            <option value="EMI_HARDSHIP">Loan &amp; EMI Financial Hardship / Assistance</option>
                            <option value="GENERAL_INQUIRY">General Banking &amp; Problem Solving</option>
                        </select>
                    </div>

                    <div class="row g-2 mb-3">
                        <div class="col-6">
                            <label class="form-label fw-semibold">Priority</label>
                            <select class="form-select" name="priority">
                                <option value="LOW">Low</option>
                                <option value="MEDIUM" selected>Medium</option>
                                <option value="HIGH">High</option>
                                <option value="EMERGENCY">Emergency</option>
                            </select>
                        </div>
                        <div class="col-6">
                            <label class="form-label fw-semibold">Disputed Amount (₹)</label>
                            <input type="number" class="form-control" name="disputedAmount" placeholder="e.g. 5000">
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Transaction Reference No (Optional)</label>
                        <input type="text" class="form-control font-monospace" name="transactionReference" placeholder="e.g. TXN-20261021-00106" maxlength="50">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Subject</label>
                        <input type="text" class="form-control" name="subject" placeholder="Brief summary of the issue" required maxlength="150">
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Detailed Description</label>
                        <textarea class="form-control" name="description" rows="4" placeholder="Describe the problem, location, time, and resolution requested in detail..." required></textarea>
                    </div>

                    <button type="submit" class="btn btn-primary w-100 py-2 shadow-sm">
                        <i class="bi bi-send-fill me-1"></i> Submit Problem Solving Ticket
                    </button>
                </form>
            </div>
        </div>

        <!-- My Tickets History -->
        <div class="col-lg-7">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex justify-content-between align-items-center mb-3 border-bottom pb-2">
                    <h5 class="fw-bold mb-0">
                        <i class="bi bi-clock-history text-primary me-2"></i>My Support Tickets &amp; Status
                    </h5>
                    <button class="btn btn-sm btn-outline-primary" onclick="window.location.reload()">
                        <i class="bi bi-arrow-clockwise me-1"></i> Refresh
                    </button>
                </div>

                <div class="table-responsive">
                    <table class="table table-bank mb-0">
                        <thead>
                            <tr>
                                <th>Ticket No</th>
                                <th>Issue Type</th>
                                <th>Subject</th>
                                <th>Status</th>
                                <th>Date</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty myTickets}">
                                    <c:forEach var="t" items="${myTickets}">
                                        <tr>
                                            <td>
                                                <code class="fw-bold text-primary"><c:out value="${t.ticketNumber}" /></code>
                                                <c:if test="${t.priority == 'EMERGENCY' || t.priority == 'HIGH'}">
                                                    <span class="badge bg-danger ms-1">${t.priority}</span>
                                                </c:if>
                                            </td>
                                            <td><small><c:out value="${t.issueType.displayName}" /></small></td>
                                            <td>
                                                <strong><c:out value="${t.subject}" /></strong>
                                                <c:if test="${not empty t.resolutionRemarks}">
                                                    <div class="mt-1 p-2 rounded bg-dark border border-success small text-success">
                                                        <i class="bi bi-chat-left-dots-fill me-1"></i>
                                                        <strong>Bank Resolution:</strong> <c:out value="${t.resolutionRemarks}" />
                                                        <c:if test="${not empty t.resolvedBy}">
                                                            <span class="text-muted small">(Officer: ${t.resolvedBy})</span>
                                                        </c:if>
                                                    </div>
                                                </c:if>
                                            </td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${t.status == 'OPEN'}">
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-envelope-open me-1"></i>Open</span>
                                                    </c:when>
                                                    <c:when test="${t.status == 'IN_PROGRESS'}">
                                                        <span class="badge bg-info"><i class="bi bi-gear-fill me-1"></i>Investigating</span>
                                                    </c:when>
                                                    <c:when test="${t.status == 'RESOLVED'}">
                                                        <span class="badge bg-success"><i class="bi bi-check-all me-1"></i>Resolved</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary"><c:out value="${t.status}" /></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${t.createdAt}" pattern="dd-MMM HH:mm" /></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="5" class="text-center py-5 text-muted">
                                            <i class="bi bi-check2-circle fs-1 text-success d-block mb-2"></i>
                                            No active support issues or disputes on record.<br>
                                            Your accounts and cards are operating normally.
                                        </td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- ==================================================================== -->
<!-- ATM Cash Not Dispensed Dispute Modal -->
<!-- ==================================================================== -->
<div class="modal fade" id="atmDisputeModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content card-bank border-warning">
            <div class="modal-header border-secondary">
                <h5 class="modal-title fw-bold text-warning">
                    <i class="bi bi-cash-stack me-2"></i>ATM Cash Not Dispensed Dispute
                </h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form action="${pageContext.request.contextPath}/atm/support" method="POST">
                <input type="hidden" name="action" value="failedAtmCash">
                <div class="modal-body">
                    <div class="alert alert-warning small mb-3">
                        <i class="bi bi-info-circle-fill me-1"></i>
                        Under RBI circular DPSS.PD.No.2632/02.10.002/2010-2011, bank must resolve failed ATM dispense within 5 calendar days or pay ₹100/day compensation.
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Disputed Amount (₹)</label>
                        <div class="input-group">
                            <span class="input-group-text">₹</span>
                            <input type="number" class="form-control" name="disputedAmount" placeholder="e.g. 2000" min="100" required>
                        </div>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">ATM Terminal ID / Location</label>
                        <input type="text" class="form-control" name="atmTerminalId" value="APEX-ATM-042 (Connaught Place Center)" required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label fw-semibold">Transaction Reference Number (From SMS or Slip)</label>
                        <input type="text" class="form-control font-monospace" name="transactionReference" placeholder="e.g. TXN-20261021-00106">
                    </div>
                </div>
                <div class="modal-footer border-secondary">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-warning text-dark fw-bold">
                        <i class="bi bi-send-check me-1"></i> Submit Official Dispute
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
