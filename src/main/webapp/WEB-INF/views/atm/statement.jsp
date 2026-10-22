<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Full Account Statement - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <jsp:include page="../common/messages.jsp" />

    <!-- Statement Header -->
    <div class="card card-bank p-4 mb-4">
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-file-earmark-spreadsheet text-primary me-2"></i>Full Account Statement</h4>
                <small class="text-secondary">Comprehensive ledger with filtering, keyword search, and formal PDF generation</small>
            </div>
            <div class="d-flex gap-2 mt-2 mt-md-0">
                <a href="${pageContext.request.contextPath}/atm/statement/pdf?type=${selectedType}&startDate=${startDate}&endDate=${endDate}&search=${searchQuery}" 
                   class="btn btn-danger">
                    <i class="bi bi-file-earmark-pdf me-1"></i> Export PDF Statement
                </a>
                <button type="button" class="btn btn-outline-secondary" onclick="window.print()">
                    <i class="bi bi-printer me-1"></i> Print View
                </button>
            </div>
        </div>

        <!-- Filter & Search Toolbar Form -->
        <form action="${pageContext.request.contextPath}/atm/statement" method="GET" class="row g-2 align-items-end pt-3 border-top">
            <div class="col-md-3">
                <label for="type" class="form-label small fw-semibold text-secondary">Transaction Type</label>
                <select class="form-select form-select-sm" id="type" name="type">
                    <option value="ALL" ${selectedType eq 'ALL' ? 'selected' : ''}>All Types</option>
                    <option value="DEPOSIT" ${selectedType eq 'DEPOSIT' ? 'selected' : ''}>Deposits</option>
                    <option value="WITHDRAWAL" ${selectedType eq 'WITHDRAWAL' ? 'selected' : ''}>Withdrawals</option>
                    <option value="FAST_CASH" ${selectedType eq 'FAST_CASH' ? 'selected' : ''}>Fast Cash</option>
                    <option value="TRANSFER_OUT" ${selectedType eq 'TRANSFER_OUT' ? 'selected' : ''}>Transfers (Out)</option>
                    <option value="TRANSFER_IN" ${selectedType eq 'TRANSFER_IN' ? 'selected' : ''}>Transfers (In)</option>
                </select>
            </div>

            <div class="col-md-2">
                <label for="startDate" class="form-label small fw-semibold text-secondary">From Date</label>
                <input type="date" class="form-control form-control-sm" id="startDate" name="startDate" value="${startDate}">
            </div>

            <div class="col-md-2">
                <label for="endDate" class="form-label small fw-semibold text-secondary">To Date</label>
                <input type="date" class="form-control form-control-sm" id="endDate" name="endDate" value="${endDate}">
            </div>

            <div class="col-md-2">
                <label for="sort" class="form-label small fw-semibold text-secondary">Sort By</label>
                <select class="form-select form-select-sm" id="sort" name="sort">
                    <option value="date_desc" ${selectedSort eq 'date_desc' ? 'selected' : ''}>Date (Newest First)</option>
                    <option value="date_asc" ${selectedSort eq 'date_asc' ? 'selected' : ''}>Date (Oldest First)</option>
                    <option value="amount_desc" ${selectedSort eq 'amount_desc' ? 'selected' : ''}>Amount (High to Low)</option>
                    <option value="amount_asc" ${selectedSort eq 'amount_asc' ? 'selected' : ''}>Amount (Low to High)</option>
                </select>
            </div>

            <div class="col-md-2">
                <label for="search" class="form-label small fw-semibold text-secondary">Keyword Search</label>
                <input type="text" class="form-control form-control-sm" id="search" name="search" 
                       placeholder="Ref No / Note" value="${searchQuery}">
            </div>

            <div class="col-md-1">
                <button type="submit" class="btn btn-primary btn-sm w-100">
                    <i class="bi bi-funnel-fill"></i> Filter
                </button>
            </div>
        </form>
    </div>

    <!-- Statement Table -->
    <div class="card card-bank p-4">
        <div class="d-flex justify-content-between align-items-center mb-3">
            <span class="text-secondary small">
                Showing <strong>${transactions.size()}</strong> record(s) for Account: <strong class="font-monospace"><c:out value="${maskedAccount}" /></strong>
            </span>
            <span class="text-success fw-bold">
                Available Balance: <c:out value="${currentBalance}" />
            </span>
        </div>

        <div class="table-responsive">
            <table class="table table-bank mb-0">
                <thead>
                    <tr>
                        <th>Date &amp; Time</th>
                        <th>Reference Number</th>
                        <th>Type</th>
                        <th>Channel</th>
                        <th>Description</th>
                        <th class="text-end">Amount (INR)</th>
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
                                                <span class="badge-credit"><i class="bi bi-plus-circle me-1"></i><c:out value="${t.transactionType.description}" /></span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge-debit"><i class="bi bi-dash-circle me-1"></i><c:out value="${t.transactionType.description}" /></span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td><span class="badge bg-secondary-subtle text-secondary"><c:out value="${t.channel}" /></span></td>
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
                                <td colspan="7" class="text-center py-5 text-muted">
                                    <i class="bi bi-search fs-1 d-block mb-2 text-secondary"></i>
                                    No transactions match your search filter criteria.
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
