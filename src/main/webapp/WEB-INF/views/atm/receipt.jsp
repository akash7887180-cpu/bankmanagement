<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="ATM Transaction Advice - Receipt" />
<c:set var="isReceiptPage" value="true" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <!-- Success Banner -->
    <div class="alert alert-success text-center mb-3 shadow-sm">
        <i class="bi bi-check-circle-fill me-2 fs-5"></i>
        <strong>Transaction Completed Successfully!</strong> An electronic receipt has been generated.
    </div>

    <!-- Realistic Thermal Paper Receipt Mockup -->
    <div class="receipt-wrapper">
        <div class="receipt-paper" id="printableReceipt">
            <!-- Bank Header -->
            <div class="receipt-header">
                <div class="receipt-bank-title"><c:out value="${receipt.bankName}" /></div>
                <div class="receipt-bank-subtitle">ATM TRANSACTION ADVICE</div>
                <div class="small text-muted"><c:out value="${receipt.branchName}" /></div>
                <div class="small text-muted">IFSC: <c:out value="${receipt.ifscCode}" /></div>
            </div>

            <!-- Metadata Section -->
            <div class="receipt-row">
                <span class="receipt-label">DATE:</span>
                <span class="receipt-val"><c:out value="${receipt.formattedDate}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">TIME:</span>
                <span class="receipt-val"><c:out value="${receipt.formattedTime}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">ATM ID:</span>
                <span class="receipt-val"><c:out value="${receipt.atmId}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">TERMINAL ID:</span>
                <span class="receipt-val"><c:out value="${receipt.terminalId}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">TXN REF NO:</span>
                <span class="receipt-val"><c:out value="${receipt.transactionReference}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">RECEIPT NO:</span>
                <span class="receipt-val"><c:out value="${receipt.receiptNumber}" /></span>
            </div>

            <div class="receipt-divider"></div>

            <!-- Account Details -->
            <div class="receipt-row">
                <span class="receipt-label">CARD NUMBER:</span>
                <span class="receipt-val"><c:out value="${receipt.maskedCard}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">ACCOUNT NO:</span>
                <span class="receipt-val"><c:out value="${receipt.maskedAccount}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">CARDHOLDER:</span>
                <span class="receipt-val"><c:out value="${receipt.customerName}" /></span>
            </div>
            <div class="receipt-row">
                <span class="receipt-label">TRANSACTION:</span>
                <span class="receipt-val text-uppercase"><c:out value="${receipt.transactionType.description}" /></span>
            </div>

            <!-- Amount Box -->
            <div class="receipt-amount-box">
                <div class="small text-muted text-uppercase">Transaction Amount</div>
                <div class="amount-num"><c:out value="${formattedAmount}" /></div>
                <div class="small text-muted mt-1">(<c:out value="${amountInWords}" />)</div>
            </div>

            <!-- Balance -->
            <div class="receipt-row">
                <span class="receipt-label">AVAILABLE BAL:</span>
                <span class="receipt-val fs-6"><c:out value="${formattedBalance}" /></span>
            </div>

            <div class="receipt-divider"></div>

            <!-- Barcode -->
            <div class="receipt-barcode">
                <div class="barcode-lines"></div>
                <div class="small text-muted mt-1 font-monospace">*<c:out value="${receipt.receiptNumber}" />*</div>
            </div>

            <!-- Footer Section -->
            <div class="receipt-footer">
                <p class="mb-1 fw-bold"><c:out value="${receipt.thankYouMessage}" /></p>
                <p class="mb-0">24x7 Customer Helpline: <strong><c:out value="${receipt.customerCarePhone}" /></strong></p>
                <p class="small text-muted mt-1 mb-0">Please retain this advice slip for your records.</p>
            </div>
        </div>

        <!-- Receipt Action Buttons -->
        <div class="receipt-actions">
            <a href="${pageContext.request.contextPath}/atm/receipt/pdf" class="btn btn-danger btn-sm shadow-sm">
                <i class="bi bi-file-earmark-pdf me-1"></i> Download PDF
            </a>
            <button type="button" class="btn btn-dark btn-sm shadow-sm" onclick="printReceipt()">
                <i class="bi bi-printer me-1"></i> Print Receipt
            </button>
            <button type="button" class="btn btn-outline-primary btn-sm shadow-sm" onclick="shareReceipt('Apex ATM Receipt - Ref: ${receipt.transactionReference}, Amount: ${formattedAmount}')">
                <i class="bi bi-share me-1"></i> Share Receipt
            </button>
        </div>

        <div class="text-center mt-3">
            <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-primary w-100">
                <i class="bi bi-house-door-fill me-1"></i> Return to ATM Dashboard
            </a>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
