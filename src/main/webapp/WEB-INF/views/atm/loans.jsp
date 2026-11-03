<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="pageTitle" value="Loans & Gold Loans - Apex National Bank" />
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
                    <li class="breadcrumb-item active" aria-current="page">Loans &amp; Gold Loans</li>
                </ol>
            </nav>
            <h3 class="fw-bold mb-0">
                <i class="bi bi-bank2 text-warning me-2"></i>Apex Credit &amp; Gold Loan Suite
            </h3>
        </div>
        <div class="mt-2 mt-md-0">
            <span class="badge bg-success-subtle text-success border border-success px-3 py-2">
                <i class="bi bi-shield-check me-1"></i> RBI Compliant • Paperless Direct Account Credit
            </span>
        </div>
    </div>

    <!-- Navigation Pills / Tabs -->
    <ul class="nav nav-pills nav-fill mb-4 gap-2 p-1 bg-dark rounded-3" id="loanTabs" role="tablist">
        <li class="nav-item" role="presentation">
            <button class="nav-link active fw-bold py-2" id="money-tab" data-bs-toggle="tab" data-bs-target="#moneyLoanPane" type="button" role="tab">
                <i class="bi bi-cash-coin me-2 text-primary"></i>Money Loan &amp; EMI Calculator
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link fw-bold py-2" id="gold-tab" data-bs-toggle="tab" data-bs-target="#goldLoanPane" type="button" role="tab">
                <i class="bi bi-gem me-2 text-warning"></i>Instant Gold Loan (75% LTV)
            </button>
        </li>
        <li class="nav-item" role="presentation">
            <button class="nav-link fw-bold py-2" id="portfolio-tab" data-bs-toggle="tab" data-bs-target="#myLoansPane" type="button" role="tab">
                <i class="bi bi-journal-text me-2 text-info"></i>My Loan Portfolio (${empty myLoans ? '0' : myLoans.size()})
            </button>
        </li>
    </ul>

    <div class="tab-content" id="loanTabsContent">

        <!-- ==================================================================== -->
        <!-- TAB 1: MONEY LOAN & EMI CALCULATOR -->
        <!-- ==================================================================== -->
        <div class="tab-pane fade show active" id="moneyLoanPane" role="tabpanel">
            <div class="row g-4">
                <!-- Live EMI Calculator -->
                <div class="col-lg-5">
                    <div class="card card-bank p-4 h-100">
                        <div class="d-flex align-items-center justify-content-between mb-3 border-bottom pb-2">
                            <h5 class="fw-bold mb-0 text-primary">
                                <i class="bi bi-calculator me-2"></i>Live EMI Calculator
                            </h5>
                            <span class="badge bg-primary">Auto-Calculated</span>
                        </div>

                        <!-- Loan Type Selector -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold small text-muted">Select Loan Product</label>
                            <select class="form-select" id="calcLoanType" onchange="updateLoanDefaults()">
                                <option value="PERSONAL" data-rate="10.50">Personal Loan (10.5% p.a.)</option>
                                <option value="HOME" data-rate="8.40">Home Loan (8.4% p.a.)</option>
                                <option value="VEHICLE" data-rate="9.00">Vehicle / Car Loan (9.0% p.a.)</option>
                                <option value="BUSINESS" data-rate="12.00">Business Expansion Loan (12.0% p.a.)</option>
                                <option value="EDUCATION" data-rate="9.50">Higher Education Loan (9.5% p.a.)</option>
                            </select>
                        </div>

                        <!-- Principal Amount -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label fw-semibold small text-muted mb-0">Loan Amount (₹)</label>
                                <span class="fw-bold text-primary fs-5" id="calcAmountLabel">₹5,00,000</span>
                            </div>
                            <input type="range" class="form-range" id="calcAmountRange" min="25000" max="5000000" step="25000" value="500000" oninput="calculateMoneyEMI()">
                            <input type="number" class="form-control form-control-sm mt-1" id="calcAmountInput" value="500000" min="10000" max="10000000" oninput="syncAmountRange(this.value)">
                        </div>

                        <!-- Tenure Months -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label fw-semibold small text-muted mb-0">Tenure (Months)</label>
                                <span class="fw-bold text-info" id="calcTenureLabel">36 Months (3 Years)</span>
                            </div>
                            <input type="range" class="form-range" id="calcTenureRange" min="6" max="240" step="6" value="36" oninput="calculateMoneyEMI()">
                        </div>

                        <!-- Interest Rate -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label fw-semibold small text-muted mb-0">Annual Interest Rate (%)</label>
                                <span class="fw-bold text-warning" id="calcRateLabel">10.50%</span>
                            </div>
                            <input type="number" class="form-control form-control-sm" id="calcRateInput" value="10.50" step="0.10" min="1" max="30" oninput="calculateMoneyEMI()">
                        </div>

                        <!-- Calculated Results Display -->
                        <div class="p-3 rounded-3 mt-auto bg-dark border border-secondary text-center">
                            <small class="text-uppercase text-light opacity-75 fw-semibold d-block">Estimated Monthly EMI</small>
                            <h2 class="display-6 fw-bold text-success mb-2" id="calcResultEmi">₹16,252</h2>
                            <div class="row pt-2 border-top border-secondary text-center g-2 small">
                                <div class="col-6">
                                    <span class="text-muted d-block">Total Interest:</span>
                                    <strong class="text-warning" id="calcResultInterest">₹85,072</strong>
                                </div>
                                <div class="col-6">
                                    <span class="text-muted d-block">Total Payable:</span>
                                    <strong class="text-light" id="calcResultTotal">₹5,85,072</strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Money Loan Application Form -->
                <div class="col-lg-7">
                    <div class="card card-bank p-4 h-100">
                        <div class="d-flex align-items-center justify-content-between mb-3 border-bottom pb-2">
                            <h5 class="fw-bold mb-0">
                                <i class="bi bi-file-earmark-text text-primary me-2"></i>Apply for Money Loan
                            </h5>
                            <span class="badge bg-success-subtle text-success">Instant Submission</span>
                        </div>

                        <form action="${pageContext.request.contextPath}/atm/loans" method="POST" id="moneyLoanForm">
                            <input type="hidden" name="action" value="applyMoney">

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Loan Category</label>
                                    <select class="form-select" name="loanType" id="formLoanType" required>
                                        <option value="PERSONAL">Personal Loan</option>
                                        <option value="HOME">Home Loan</option>
                                        <option value="VEHICLE">Vehicle Loan</option>
                                        <option value="BUSINESS">Business Expansion</option>
                                        <option value="EDUCATION">Education Loan</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Requested Amount (₹)</label>
                                    <div class="input-group">
                                        <span class="input-group-text">₹</span>
                                        <input type="number" class="form-control" name="amount" id="formAmount" value="500000" min="10000" max="10000000" required>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Repayment Tenure (Months)</label>
                                    <input type="number" class="form-control" name="tenureMonths" id="formTenure" value="36" min="6" max="360" required>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Interest Rate (% p.a.)</label>
                                    <div class="input-group">
                                        <input type="number" class="form-control" name="interestRate" id="formRate" value="10.50" step="0.10" readonly>
                                        <span class="input-group-text">%</span>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Employment Type</label>
                                    <select class="form-select" name="employmentType" required>
                                        <option value="SALARIED">Salaried Employee</option>
                                        <option value="SELF_EMPLOYED">Self-Employed / Business</option>
                                        <option value="PROFESSIONAL">Doctor / Lawyer / CA</option>
                                        <option value="PENSIONER">Pensioner</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Monthly Income (₹)</label>
                                    <div class="input-group">
                                        <span class="input-group-text">₹</span>
                                        <input type="number" class="form-control" name="monthlyIncome" placeholder="e.g. 65000" min="15000" required>
                                    </div>
                                </div>

                                <div class="col-12">
                                    <label class="form-label fw-semibold">Purpose of Loan</label>
                                    <input type="text" class="form-control" name="purpose" placeholder="e.g. Home Renovation, Medical Expenses, Business Inventory" maxlength="250" required>
                                </div>

                                <div class="col-12">
                                    <div class="p-3 rounded bg-dark border border-secondary small">
                                        <div class="d-flex align-items-center mb-1">
                                            <i class="bi bi-info-circle-fill text-primary me-2"></i>
                                            <strong class="text-light">Direct Account Disbursal Guarantee:</strong>
                                        </div>
                                        <span class="text-muted">Upon underwriting approval by Bank Manager, approved loan funds will be automatically credited to your core account ending in <strong><c:out value="${sessionScope.SESSION_ACCOUNT.maskedAccountNumber}" /></strong> without branch queues.</span>
                                    </div>
                                </div>

                                <div class="col-12 text-end pt-2">
                                    <button type="submit" class="btn btn-primary btn-lg px-4 shadow-sm">
                                        <i class="bi bi-send-check me-1"></i> Submit Money Loan Application
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- ==================================================================== -->
        <!-- TAB 2: GOLD LOAN & LIVE VALUATION -->
        <!-- ==================================================================== -->
        <div class="tab-pane fade" id="goldLoanPane" role="tabpanel">
            <div class="row g-4">
                <!-- Live Gold Rate Board & Valuation Calculator -->
                <div class="col-lg-5">
                    <div class="card card-bank p-4 h-100">
                        <div class="d-flex align-items-center justify-content-between mb-3 border-bottom pb-2">
                            <h5 class="fw-bold mb-0 text-warning">
                                <i class="bi bi-gem me-2"></i>Today's Gold Rates (Per Gram)
                            </h5>
                            <span class="badge bg-warning text-dark"><i class="bi bi-record-circle me-1"></i>Live Market</span>
                        </div>

                        <!-- Live Benchmark Board -->
                        <div class="row g-2 mb-3 text-center">
                            <div class="col-4">
                                <div class="p-2 rounded bg-dark border border-secondary">
                                    <small class="text-muted d-block" style="font-size:0.75rem;">24K (Pure)</small>
                                    <strong class="text-warning">₹<c:out value="${goldRate24k}" /></strong>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="p-2 rounded bg-dark border border-warning">
                                    <small class="text-warning d-block fw-bold" style="font-size:0.75rem;">22K (Jewelry)</small>
                                    <strong class="text-warning fs-6">₹<c:out value="${goldRate22k}" /></strong>
                                </div>
                            </div>
                            <div class="col-4">
                                <div class="p-2 rounded bg-dark border border-secondary">
                                    <small class="text-muted d-block" style="font-size:0.75rem;">18K</small>
                                    <strong class="text-warning">₹<c:out value="${goldRate18k}" /></strong>
                                </div>
                            </div>
                        </div>

                        <!-- Gold Valuation Calculator -->
                        <h6 class="fw-bold text-primary mb-2"><i class="bi bi-calculator me-1"></i>Instant Gold Valuation</h6>

                        <!-- Weight in grams -->
                        <div class="mb-3">
                            <div class="d-flex justify-content-between align-items-center mb-1">
                                <label class="form-label fw-semibold small text-muted mb-0">Net Gold Weight (Grams)</label>
                                <span class="fw-bold text-warning fs-5" id="goldWeightLabel">40.0 g</span>
                            </div>
                            <input type="range" class="form-range" id="goldWeightRange" min="2" max="500" step="1" value="40" oninput="calculateGoldLoan()">
                            <input type="number" class="form-control form-control-sm mt-1" id="goldWeightInput" value="40" min="1" max="2000" oninput="syncGoldWeight(this.value)">
                        </div>

                        <!-- Purity dropdown -->
                        <div class="mb-3">
                            <label class="form-label fw-semibold small text-muted">Gold Purity (Karat)</label>
                            <select class="form-select form-select-sm" id="goldKaratSelect" onchange="calculateGoldLoan()">
                                <option value="22" selected>22 Karat (Jewelry Standard - ₹6,875/g)</option>
                                <option value="24">24 Karat (99.9% Pure Coins/Bars - ₹7,500/g)</option>
                                <option value="18">18 Karat (Studded/Ornamental - ₹5,625/g)</option>
                            </select>
                        </div>

                        <!-- Gold Results Card -->
                        <div class="p-3 rounded-3 mt-auto bg-dark border border-warning text-center">
                            <small class="text-uppercase text-light opacity-75 fw-semibold d-block">Eligible Cash Loan (75% LTV)</small>
                            <h2 class="display-6 fw-bold text-warning mb-2" id="goldResultEligible">₹2,06,250</h2>
                            <div class="row pt-2 border-top border-secondary text-center g-2 small">
                                <div class="col-6">
                                    <span class="text-muted d-block">Gross Gold Value:</span>
                                    <strong class="text-light" id="goldResultGross">₹2,75,000</strong>
                                </div>
                                <div class="col-6">
                                    <span class="text-muted d-block">Interest Rate:</span>
                                    <strong class="text-success">0.75% / month (9% p.a.)</strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Gold Loan Application Form -->
                <div class="col-lg-7">
                    <div class="card card-bank p-4 h-100">
                        <div class="d-flex align-items-center justify-content-between mb-3 border-bottom pb-2">
                            <h5 class="fw-bold mb-0">
                                <i class="bi bi-shield-lock-fill text-warning me-2"></i>Pledge Gold &amp; Apply
                            </h5>
                            <span class="badge bg-warning text-dark">Instant 15-Min Disbursal</span>
                        </div>

                        <form action="${pageContext.request.contextPath}/atm/loans" method="POST" id="goldLoanForm">
                            <input type="hidden" name="action" value="applyGold">

                            <div class="row g-3">
                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Net Gold Weight (Grams)</label>
                                    <div class="input-group">
                                        <input type="number" step="0.1" class="form-control" name="goldWeightGrams" id="formGoldWeight" value="40.0" min="1" max="2000" required>
                                        <span class="input-group-text">grams</span>
                                    </div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Karat Purity</label>
                                    <select class="form-select" name="goldKarat" id="formGoldKarat" required>
                                        <option value="22" selected>22 Karat (Jewelry Standard)</option>
                                        <option value="24">24 Karat (Pure)</option>
                                        <option value="18">18 Karat</option>
                                    </select>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Requested Loan Amount (₹)</label>
                                    <div class="input-group">
                                        <span class="input-group-text">₹</span>
                                        <input type="number" class="form-control" name="requestedAmount" id="formGoldAmount" value="200000" min="5000" required>
                                    </div>
                                    <div class="form-text small">Leave empty or up to max 75% valuation.</div>
                                </div>

                                <div class="col-md-6">
                                    <label class="form-label fw-semibold">Tenure (Months)</label>
                                    <select class="form-select" name="tenureMonths">
                                        <option value="6">6 Months (Short Term)</option>
                                        <option value="12" selected>12 Months (1 Year - Standard)</option>
                                        <option value="24">24 Months (2 Years)</option>
                                        <option value="36">36 Months (3 Years)</option>
                                    </select>
                                </div>

                                <div class="col-12">
                                    <label class="form-label fw-semibold">Ornament Description</label>
                                    <input type="text" class="form-control" name="ornamentDetails" placeholder="e.g. 2 Gold Bangles + 1 Gold Chain (Hallmarked 916)" maxlength="250" required>
                                </div>

                                <div class="col-12">
                                    <div class="p-3 rounded bg-dark border border-secondary small">
                                        <div class="d-flex align-items-center mb-1">
                                            <i class="bi bi-safe2-fill text-warning me-2"></i>
                                            <strong class="text-light">Safe Custody &amp; Insurance Guarantee:</strong>
                                        </div>
                                        <span class="text-muted">Pledged gold ornaments are securely sealed in a tamper-evident bank vault with 100% insurance coverage. When redeemed, your exact ornaments are returned in original condition.</span>
                                    </div>
                                </div>

                                <div class="col-12 text-end pt-2">
                                    <button type="submit" class="btn btn-warning btn-lg px-4 shadow-sm text-dark fw-bold">
                                        <i class="bi bi-gem me-1"></i> Submit Gold Loan Application
                                    </button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <!-- ==================================================================== -->
        <!-- TAB 3: MY LOAN PORTFOLIO -->
        <!-- ==================================================================== -->
        <div class="tab-pane fade" id="myLoansPane" role="tabpanel">
            <div class="card card-bank p-4">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <h5 class="fw-bold mb-0">
                        <i class="bi bi-journal-check text-primary me-2"></i>My Applied Loan Portfolio
                    </h5>
                    <button class="btn btn-sm btn-outline-primary" onclick="window.location.reload()">
                        <i class="bi bi-arrow-clockwise me-1"></i> Refresh
                    </button>
                </div>

                <div class="table-responsive">
                    <table class="table table-bank mb-0">
                        <thead>
                            <tr>
                                <th>Reference No</th>
                                <th>Category</th>
                                <th>Type</th>
                                <th class="text-end">Amount</th>
                                <th class="text-end">Monthly EMI</th>
                                <th>Tenure</th>
                                <th>Status</th>
                                <th>Applied Date</th>
                                <th>Bank Remarks</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:choose>
                                <c:when test="${not empty myLoans}">
                                    <c:forEach var="l" items="${myLoans}">
                                        <tr>
                                            <td><code class="fw-bold text-primary"><c:out value="${l.loanReference}" /></code></td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${l.loanCategory == 'GOLD_LOAN'}">
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-gem me-1"></i>Gold Loan</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-primary"><i class="bi bi-cash-coin me-1"></i>Money Loan</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><c:out value="${l.loanType}" /></td>
                                            <td class="text-end fw-bold text-light"><c:out value="${l.formattedAmount}" /></td>
                                            <td class="text-end fw-bold text-success"><c:out value="${l.formattedEmi}" />/mo</td>
                                            <td><c:out value="${l.tenureMonths}" /> Mos</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${l.status == 'PENDING'}">
                                                        <span class="badge bg-warning text-dark"><i class="bi bi-hourglass-split me-1"></i>Pending</span>
                                                    </c:when>
                                                    <c:when test="${l.status == 'APPROVED'}">
                                                        <span class="badge bg-info"><i class="bi bi-check-circle me-1"></i>Approved</span>
                                                    </c:when>
                                                    <c:when test="${l.status == 'DISBURSED'}">
                                                        <span class="badge bg-success"><i class="bi bi-cash-stack me-1"></i>Disbursed</span>
                                                    </c:when>
                                                    <c:when test="${l.status == 'REJECTED'}">
                                                        <span class="badge bg-danger"><i class="bi bi-x-circle me-1"></i>Rejected</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="badge bg-secondary"><c:out value="${l.status}" /></span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${l.appliedDate}" pattern="dd-MMM-yyyy HH:mm" /></td>
                                            <td class="small text-muted"><c:out value="${l.adminRemarks}" /></td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="9" class="text-center py-5 text-muted">
                                            <i class="bi bi-inbox fs-1 d-block mb-2"></i>
                                            You have not submitted any loan applications yet.<br>
                                            Use the <strong>Money Loan</strong> or <strong>Gold Loan</strong> tabs above to calculate and apply!
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

<script>
// Live Money Loan Calculator
function calculateMoneyEMI() {
    const p = parseFloat(document.getElementById("calcAmountRange").value) || 0;
    const n = parseInt(document.getElementById("calcTenureRange").value) || 12;
    const annualRate = parseFloat(document.getElementById("calcRateInput").value) || 10.5;

    document.getElementById("calcAmountLabel").innerText = "₹" + p.toLocaleString('en-IN');
    document.getElementById("calcAmountInput").value = p;
    document.getElementById("formAmount").value = p;

    const years = (n / 12).toFixed(1);
    document.getElementById("calcTenureLabel").innerText = n + " Months (" + years + " Yrs)";
    document.getElementById("formTenure").value = n;

    document.getElementById("calcRateLabel").innerText = annualRate.toFixed(2) + "%";
    document.getElementById("formRate").value = annualRate.toFixed(2);

    const r = (annualRate / 100) / 12;
    let emi = 0;
    if (r > 0) {
        emi = (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1);
    } else {
        emi = p / n;
    }
    const totalPayable = emi * n;
    const totalInterest = totalPayable - p;

    document.getElementById("calcResultEmi").innerText = "₹" + Math.round(emi).toLocaleString('en-IN');
    document.getElementById("calcResultInterest").innerText = "₹" + Math.round(totalInterest).toLocaleString('en-IN');
    document.getElementById("calcResultTotal").innerText = "₹" + Math.round(totalPayable).toLocaleString('en-IN');
}

function syncAmountRange(val) {
    document.getElementById("calcAmountRange").value = val;
    calculateMoneyEMI();
}

function updateLoanDefaults() {
    const sel = document.getElementById("calcLoanType");
    const opt = sel.options[sel.selectedIndex];
    const rate = opt.getAttribute("data-rate");
    document.getElementById("calcRateInput").value = rate;
    document.getElementById("formLoanType").value = sel.value;
    calculateMoneyEMI();
}

// Live Gold Loan Calculator
function calculateGoldLoan() {
    const grams = parseFloat(document.getElementById("goldWeightRange").value) || 0;
    const karat = parseInt(document.getElementById("goldKaratSelect").value) || 22;

    document.getElementById("goldWeightLabel").innerText = grams.toFixed(1) + " g";
    document.getElementById("goldWeightInput").value = grams;
    document.getElementById("formGoldWeight").value = grams;
    document.getElementById("formGoldKarat").value = karat;

    let rate = 6875;
    if (karat === 24) rate = 7500;
    else if (karat === 18) rate = 5625;

    const gross = grams * rate;
    const eligible = Math.round(gross * 0.75);

    document.getElementById("goldResultGross").innerText = "₹" + Math.round(gross).toLocaleString('en-IN');
    document.getElementById("goldResultEligible").innerText = "₹" + eligible.toLocaleString('en-IN');
    document.getElementById("formGoldAmount").value = eligible;
}

function syncGoldWeight(val) {
    document.getElementById("goldWeightRange").value = val;
    calculateGoldLoan();
}

// Auto-switch to tab based on URL hash (e.g. #gold or #money)
document.addEventListener("DOMContentLoaded", function() {
    calculateMoneyEMI();
    calculateGoldLoan();

    if (window.location.hash === "#gold") {
        const tab = new bootstrap.Tab(document.getElementById('gold-tab'));
        tab.show();
    } else if (window.location.hash === "#portfolio") {
        const tab = new bootstrap.Tab(document.getElementById('portfolio-tab'));
        tab.show();
    }
});
</script>

<jsp:include page="../common/footer.jsp" />
