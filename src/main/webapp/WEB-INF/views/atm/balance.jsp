<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Balance Enquiry - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="text-center mb-4 pb-2 border-bottom">
                    <div class="stat-icon blue mx-auto mb-2">
                        <i class="bi bi-wallet-fill"></i>
                    </div>
                    <h4 class="fw-bold mb-1">Account Balance Enquiry</h4>
                    <p class="text-secondary small">Live ledger status from Core Banking System</p>
                </div>

                <div class="bg-primary-subtle text-primary p-4 rounded-3 text-center mb-4 border border-primary">
                    <small class="text-uppercase fw-semibold tracking-wider d-block mb-1">Current Available Balance</small>
                    <h1 class="display-5 fw-bold mb-1 text-primary"><c:out value="${currentBalance}" /></h1>
                    <div class="small fst-italic text-secondary"><c:out value="${balanceInWords}" /></div>
                </div>

                <div class="list-group list-group-flush mb-4">
                    <div class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                        <span class="text-secondary">Customer Name:</span>
                        <strong class="text-end"><c:out value="${customerName}" /></strong>
                    </div>
                    <div class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                        <span class="text-secondary">Account Number:</span>
                        <strong class="font-monospace text-end"><c:out value="${maskedAccount}" /></strong>
                    </div>
                    <div class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                        <span class="text-secondary">Account Type:</span>
                        <span class="badge bg-primary"><c:out value="${accountType}" /></span>
                    </div>
                    <div class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                        <span class="text-secondary">Minimum Balance Threshold:</span>
                        <span class="fw-semibold"><c:out value="${minimumBalance}" /></span>
                    </div>
                    <div class="list-group-item d-flex justify-content-between align-items-center px-0 bg-transparent">
                        <span class="text-secondary">Daily ATM Withdrawal Limit Left:</span>
                        <span class="fw-bold text-success"><c:out value="${dailyLimitRemaining}" /></span>
                    </div>
                </div>

                <div class="d-grid gap-2">
                    <a href="${pageContext.request.contextPath}/atm/ministatement" class="btn btn-outline-primary">
                        <i class="bi bi-receipt me-1"></i> View Mini Statement
                    </a>
                    <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-primary">
                        Back to Dashboard
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
