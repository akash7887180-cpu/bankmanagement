<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Accounts & Cards - Apex Admin" />
<jsp:include page="../common/header.jsp" />

<!-- Admin Top Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary px-3">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="28" height="28" class="me-2">
            <span>APEX BANK ADMIN</span>
            <span class="badge bg-danger ms-2">ACCOUNTS &amp; CARDS</span>
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

    <!-- Accounts Table -->
    <div class="card card-bank p-4 mb-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-bank text-primary me-2"></i>Bank Accounts</h4>
                <small class="text-secondary">Core customer accounts, balances, and status controls</small>
            </div>
            <div>
                <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#openAccountModal">
                    <i class="bi bi-plus-circle me-1"></i> Open Account &amp; Issue ATM Card
                </button>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-bank table-hover align-middle mb-0">
                <thead>
                    <tr>
                        <th>Account No</th>
                        <th>Customer ID</th>
                        <th>Account Type</th>
                        <th>Ledger Balance</th>
                        <th>Min Balance</th>
                        <th>Overdraft Limit</th>
                        <th>Status</th>
                        <th class="text-center">Control Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="acc" items="${accounts}">
                        <tr>
                            <td class="fw-bold font-monospace text-primary"><c:out value="${acc.accountNumber}" /></td>
                            <td>Customer #<c:out value="${acc.customerId}" /></td>
                            <td><span class="badge bg-info text-dark"><c:out value="${acc.accountType}" /></span></td>
                            <td class="fw-bold fs-6">₹<c:out value="${acc.balance}" /></td>
                            <td>₹<c:out value="${acc.minimumBalance}" /></td>
                            <td>₹<c:out value="${acc.overdraftLimit}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${acc.status.name() eq 'ACTIVE'}">
                                        <span class="badge bg-success"><i class="bi bi-check-circle me-1"></i>ACTIVE</span>
                                    </c:when>
                                    <c:when test="${acc.status.name() eq 'FROZEN'}">
                                        <span class="badge bg-warning text-dark"><i class="bi bi-snow me-1"></i>FROZEN</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-danger"><c:out value="${acc.status}" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${acc.status.name() eq 'ACTIVE'}">
                                        <a href="${pageContext.request.contextPath}/admin/accounts?action=freeze&id=${acc.accountId}" 
                                           class="btn btn-warning btn-sm fw-semibold" 
                                           onclick="return confirm('Freeze Account ${acc.accountNumber}? This will immediately block all withdrawals and transfers.');">
                                            <i class="bi bi-lock-fill me-1"></i> Freeze Account
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/admin/accounts?action=activate&id=${acc.accountId}" 
                                           class="btn btn-success btn-sm fw-semibold" 
                                           onclick="return confirm('Unfreeze and activate Account ${acc.accountNumber}?');">
                                            <i class="bi bi-unlock-fill me-1"></i> Activate Account
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <!-- ATM Cards Table -->
    <div class="card card-bank p-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-credit-card-2-front text-warning me-2"></i>ATM Debit Cards</h4>
                <small class="text-secondary">Security card status, wrong PIN attempt locks, and card reset</small>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-bank table-hover align-middle mb-0">
                <thead>
                    <tr>
                        <th>Card ID</th>
                        <th>Card Number</th>
                        <th>Scheme</th>
                        <th>Account ID</th>
                        <th>Customer ID</th>
                        <th>Failed PIN Tries</th>
                        <th>Daily Limit</th>
                        <th>Status</th>
                        <th class="text-center">Security Action</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="card" items="${cards}">
                        <tr>
                            <td>#<c:out value="${card.cardId}" /></td>
                            <td>
                                <div class="fw-bold font-monospace text-primary"><c:out value="${card.formattedCardNumber}" /></div>
                                <span class="badge bg-dark font-monospace"><i class="bi bi-key-fill text-warning me-1"></i>PIN: <c:out value="${card.pinPlain}" /></span>
                            </td>
                            <td><span class="badge bg-secondary"><c:out value="${card.cardType.displayName}" /></span></td>
                            <td>Acc #<c:out value="${card.accountId}" /></td>
                            <td>Cust #<c:out value="${card.customerId}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${card.failedPinAttempts > 0}">
                                        <span class="badge bg-danger"><c:out value="${card.failedPinAttempts}" /> / 3 Failed</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-success-subtle text-success">0 (Clean)</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>₹<c:out value="${card.dailyLimit}" /></td>
                            <td>
                                <c:choose>
                                    <c:when test="${card.status.name() eq 'ACTIVE'}">
                                        <span class="badge bg-success">ACTIVE</span>
                                    </c:when>
                                    <c:when test="${card.status.name() eq 'LOCKED'}">
                                        <span class="badge bg-danger"><i class="bi bi-shield-x me-1"></i>LOCKED</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-secondary"><c:out value="${card.status}" /></span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <c:if test="${card.isLocked() or card.failedPinAttempts > 0}">
                                    <a href="${pageContext.request.contextPath}/admin/accounts?action=unlock_card&id=${card.cardId}" 
                                       class="btn btn-outline-success btn-sm me-1" 
                                       onclick="return confirm('Reset failed attempts and unlock Card ${card.maskedCardNumber}?');">
                                        <i class="bi bi-key-fill me-1"></i> Reset PIN Lock
                                    </a>
                                </c:if>
                                <c:if test="${card.status.name() eq 'ACTIVE'}">
                                    <a href="${pageContext.request.contextPath}/admin/accounts?action=block_card&id=${card.cardId}" 
                                       class="btn btn-outline-danger btn-sm" 
                                       onclick="return confirm('Block Card ${card.maskedCardNumber}?');">
                                        <i class="bi bi-ban me-1"></i> Block Card
                                    </a>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Open Account & Issue ATM Card Modal -->
<div class="modal fade" id="openAccountModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-md">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/accounts" method="POST">
                <input type="hidden" name="action" value="create_account">

                <div class="modal-header">
                    <h5 class="modal-title fw-bold"><i class="bi bi-bank text-primary me-2"></i>Open Account &amp; Issue ATM Card</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body row g-3">
                    <div class="col-12">
                        <label class="form-label fw-semibold">Select Customer</label>
                        <select class="form-select" name="customerId" required>
                            <c:forEach var="c" items="${customers}">
                                <option value="${c.customerId}">
                                    #${c.customerId} - ${c.fullName} (${c.phone})
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="col-6">
                        <label class="form-label fw-semibold text-primary">Account Number <span class="text-muted fw-normal small">(Optional)</span></label>
                        <input type="text" class="form-control font-monospace" name="accountNumber" placeholder="Leave blank to auto-generate" maxlength="20">
                    </div>

                    <div class="col-6">
                        <label class="form-label fw-semibold text-primary">Card Number <span class="text-muted fw-normal small">(Optional)</span></label>
                        <input type="text" class="form-control font-monospace" name="cardNumber" placeholder="Leave blank to auto-generate" maxlength="19">
                    </div>

                    <div class="col-6">
                        <label class="form-label fw-semibold">Account Type</label>
                        <select class="form-select" name="accountType" id="accountTypeSelect" onchange="updateMinDeposit(this.value)">
                            <option value="SAVINGS">Savings Account</option>
                            <option value="CURRENT">Current Account</option>
                        </select>
                    </div>

                    <div class="col-6">
                        <label class="form-label fw-semibold">Initial Deposit (INR)</label>
                        <input type="number" class="form-control" name="initialDeposit" id="initialDepositInput" value="5000" min="1000" required>
                    </div>

                    <div class="col-6">
                        <label class="form-label fw-semibold">Card Scheme</label>
                        <select class="form-select" name="cardType">
                            <option value="DEBIT_RUPAY">RuPay Platinum</option>
                            <option value="DEBIT_VISA">Visa Signature</option>
                            <option value="DEBIT_MASTERCARD">MasterCard World</option>
                        </select>
                    </div>

                    <div class="col-6">
                        <label class="form-label fw-semibold">4-Digit ATM PIN</label>
                        <input type="text" class="form-control font-monospace fw-bold" name="pin" value="1234" maxlength="4" pattern="\d{4}" required>
                    </div>

                    <div class="col-12">
                        <div class="alert alert-info py-2 small mb-0">
                            <i class="bi bi-info-circle me-1"></i>
                            If Account Number or Card Number are left blank, unique numbers will be auto-generated automatically.
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-primary fw-bold">Open Account &amp; Activate Card</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function updateMinDeposit(type) {
    const input = document.getElementById("initialDepositInput");
    if (type === "CURRENT") {
        input.min = "5000";
        if (parseFloat(input.value) < 5000) input.value = "10000";
    } else {
        input.min = "1000";
        if (parseFloat(input.value) < 1000) input.value = "5000";
    }
}
</script>

<jsp:include page="../common/footer.jsp" />
