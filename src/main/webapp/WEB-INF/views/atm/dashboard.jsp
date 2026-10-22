<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="ATM Dashboard - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <jsp:include page="../common/messages.jsp" />

    <!-- Welcome & Live Time Bar -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-4 pb-2 border-bottom">
        <div>
            <h3 class="fw-bold mb-1">
                Welcome back, <span class="text-primary"><c:out value="${customerName}" /></span>!
            </h3>
            <span class="text-secondary small">
                <i class="bi bi-clock me-1"></i> Current Date &amp; Time: <strong><c:out value="${currentDateTime}" /></strong>
            </span>
        </div>
        <div class="text-md-end mt-2 mt-md-0">
            <span class="badge bg-primary-subtle text-primary border border-primary px-3 py-2">
                <i class="bi bi-shield-check me-1"></i> Active Session • Last Login: <c:out value="${lastLogin}" />
            </span>
        </div>
    </div>

    <!-- Top Summary Cards Row -->
    <div class="row g-4 mb-4">
        <!-- Balance Card -->
        <div class="col-lg-4 col-md-6">
            <div class="card card-bank p-4 h-100 bg-gradient text-white" style="background: linear-gradient(135deg, #1e3a8a 0%, #0f172a 100%);">
                <div class="d-flex justify-content-between align-items-start mb-3">
                    <span class="text-light opacity-75 small text-uppercase tracking-wider">Available Balance</span>
                    <button class="btn btn-sm btn-link text-white p-0" onclick="toggleBalanceVisibility()" title="Hide/Unhide Balance">
                        <i class="bi bi-eye-fill" id="eyeIcon"></i>
                    </button>
                </div>
                <h2 class="display-6 fw-bold mb-3" id="balanceDisplay"><c:out value="${availableBalance}" /></h2>
                <div class="d-flex justify-content-between align-items-center pt-2 border-top border-secondary">
                    <small class="text-light opacity-75">Account Type: <strong class="text-warning"><c:out value="${accountType}" /></strong></small>
                    <a href="${pageContext.request.contextPath}/atm/balance" class="text-decoration-none text-info small">
                        Enquiry <i class="bi bi-arrow-right"></i>
                    </a>
                </div>
            </div>
        </div>

        <!-- Account & Card Information -->
        <div class="col-lg-4 col-md-6">
            <div class="card card-bank p-4 h-100">
                <div class="d-flex justify-content-between align-items-start mb-2">
                    <span class="text-secondary small text-uppercase fw-semibold">Card &amp; Account Details</span>
                    <span class="badge bg-success-subtle text-success border border-success">
                        <c:out value="${cardType}" />
                    </span>
                </div>
                <div class="mb-2">
                    <span class="text-muted small">Masked ATM Card Number:</span>
                    <div class="fw-bold font-monospace fs-5 text-primary"><c:out value="${maskedCard}" /></div>
                </div>
                <div class="mb-0">
                    <span class="text-muted small">Account Number:</span>
                    <div class="fw-bold font-monospace fs-6"><c:out value="${maskedAccount}" /></div>
                </div>
            </div>
        </div>

        <!-- Daily Withdrawal Limit Tracker -->
        <div class="col-lg-4 col-md-12">
            <div class="card card-bank p-4 h-100">
                <span class="text-secondary small text-uppercase fw-semibold mb-2 d-block">ATM Daily Withdrawal Limit</span>
                <h3 class="fw-bold text-success mb-1"><c:out value="${dailyLimitRemaining}" /></h3>
                <small class="text-muted d-block mb-3">Remaining allowance from ₹40,000.00 daily quota.</small>
                <div class="progress" style="height: 8px;">
                    <div class="progress-bar bg-success" role="progressbar" style="width: 75%;" aria-valuenow="75" aria-valuemin="0" aria-valuemax="100"></div>
                </div>
                <div class="d-flex justify-content-between align-items-center mt-2 small text-muted">
                    <span>24-Hour Rolling Window</span>
                    <span class="text-success"><i class="bi bi-shield-lock me-1"></i>Secure</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Quick Fast Cash Row -->
    <div class="card card-bank p-4 mb-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-bold mb-0">
                <i class="bi bi-lightning-charge-fill text-warning me-2"></i>Fast Cash (1-Click Instant Dispense)
            </h5>
            <small class="text-muted">Instant withdrawal without entering amounts</small>
        </div>
        <div class="row g-2 text-center">
            <div class="col">
                <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                    <input type="hidden" name="amount" value="500">
                    <button type="submit" class="fastcash-btn w-100">
                        ₹500 <span class="sub">1-Click</span>
                    </button>
                </form>
            </div>
            <div class="col">
                <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                    <input type="hidden" name="amount" value="1000">
                    <button type="submit" class="fastcash-btn w-100">
                        ₹1,000 <span class="sub">1-Click</span>
                    </button>
                </form>
            </div>
            <div class="col">
                <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                    <input type="hidden" name="amount" value="2000">
                    <button type="submit" class="fastcash-btn w-100">
                        ₹2,000 <span class="sub">1-Click</span>
                    </button>
                </form>
            </div>
            <div class="col">
                <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                    <input type="hidden" name="amount" value="5000">
                    <button type="submit" class="fastcash-btn w-100">
                        ₹5,000 <span class="sub">1-Click</span>
                    </button>
                </form>
            </div>
            <div class="col">
                <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                    <input type="hidden" name="amount" value="10000">
                    <button type="submit" class="fastcash-btn w-100">
                        ₹10,000 <span class="sub">1-Click</span>
                    </button>
                </form>
            </div>
        </div>
    </div>

    <!-- Main Banking Operations Grid -->
    <h5 class="fw-bold mb-3"><i class="bi bi-grid-fill text-primary me-2"></i>ATM Banking Operations</h5>
    <div class="row g-3 mb-5">
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/withdraw" class="action-tile text-center">
                <i class="bi bi-cash-stack"></i>
                <span class="fw-bold">Cash Withdrawal</span>
                <small class="text-muted">Dispense currency notes</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/deposit" class="action-tile text-center">
                <i class="bi bi-wallet2 text-success"></i>
                <span class="fw-bold">Deposit Cash</span>
                <small class="text-muted">Instant credit to ledger</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/transfer" class="action-tile text-center">
                <i class="bi bi-arrow-left-right text-info"></i>
                <span class="fw-bold">Money Transfer</span>
                <small class="text-muted">Instant inter-account send</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/balance" class="action-tile text-center">
                <i class="bi bi-search text-warning"></i>
                <span class="fw-bold">Balance Enquiry</span>
                <small class="text-muted">Check ledger balance</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/ministatement" class="action-tile text-center">
                <i class="bi bi-receipt"></i>
                <span class="fw-bold">Mini Statement</span>
                <small class="text-muted">View last 10 entries</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/statement" class="action-tile text-center">
                <i class="bi bi-file-earmark-spreadsheet text-danger"></i>
                <span class="fw-bold">Full Statement</span>
                <small class="text-muted">Filter, Search &amp; PDF</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/changepin" class="action-tile text-center">
                <i class="bi bi-key-fill text-warning"></i>
                <span class="fw-bold">Change PIN</span>
                <small class="text-muted">Update security code</small>
            </a>
        </div>
        <div class="col-6 col-md-3">
            <a href="${pageContext.request.contextPath}/atm/kiosk" class="action-tile text-center border-primary bg-primary-subtle">
                <i class="bi bi-terminal-fill text-primary"></i>
                <span class="fw-bold text-primary">ATM Kiosk Mode</span>
                <small class="text-primary opacity-75">Realistic Hardware Simulator</small>
            </a>
        </div>
    </div>

    <!-- Recent Activity Table -->
    <div class="card card-bank p-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <h5 class="fw-bold mb-0"><i class="bi bi-clock-history me-2 text-primary"></i>Recent Transactions</h5>
            <a href="${pageContext.request.contextPath}/atm/statement" class="btn btn-sm btn-outline-primary">
                View Full Statement <i class="bi bi-arrow-right"></i>
            </a>
        </div>

        <div class="table-responsive">
            <table class="table table-bank mb-0">
                <thead>
                    <tr>
                        <th>Date &amp; Time</th>
                        <th>Reference No</th>
                        <th>Transaction Type</th>
                        <th>Description</th>
                        <th class="text-end">Amount</th>
                        <th class="text-end">Balance After</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty recentTransactions}">
                            <c:forEach var="t" items="${recentTransactions}">
                                <tr>
                                    <td><c:out value="${t.formattedDate}" /></td>
                                    <td><code class="fw-bold"><c:out value="${t.referenceNumber}" /></code></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${t.transactionType.credit}">
                                                <span class="badge-credit"><i class="bi bi-arrow-down-left me-1"></i><c:out value="${t.transactionType.description}" /></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-debit"><i class="bi bi-arrow-up-right me-1"></i><c:out value="${t.transactionType.description}" /></span>
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
                                <td colspan="6" class="text-center py-4 text-muted">No recent transactions recorded.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script>
let balanceVisible = true;
const rawBalanceText = "${availableBalance}";

function toggleBalanceVisibility() {
    const disp = document.getElementById("balanceDisplay");
    const icon = document.getElementById("eyeIcon");
    if (balanceVisible) {
        disp.innerText = "••••••••";
        icon.className = "bi bi-eye-slash-fill";
        balanceVisible = false;
    } else {
        disp.innerText = rawBalanceText;
        icon.className = "bi bi-eye-fill";
        balanceVisible = true;
    }
}
</script>

<jsp:include page="../common/footer.jsp" />
