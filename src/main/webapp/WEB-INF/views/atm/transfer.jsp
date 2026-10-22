<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Money Transfer - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="d-flex align-items-center mb-4 pb-2 border-bottom">
                    <div class="stat-icon purple me-3">
                        <i class="bi bi-arrow-left-right"></i>
                    </div>
                    <div>
                        <h4 class="fw-bold mb-0">Inter-Account Money Transfer</h4>
                        <small class="text-secondary">Instant atomic fund settlement between bank accounts</small>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/atm/transfer" method="POST">
                    <div class="mb-3">
                        <label for="targetAccount" class="form-label fw-semibold">Beneficiary Account Number</label>
                        <input type="text" class="form-control fw-bold font-monospace" id="targetAccount" name="targetAccount" 
                               placeholder="e.g. 1001008963" required autofocus value="${prevTarget}">
                        <div class="form-text">
                            Test beneficiary accounts: <code>1001008963</code> (Priya Sharma) or <code>1001007741</code> (Rahul Verma)
                        </div>
                    </div>

                    <div class="mb-3">
                        <label for="confirmAccount" class="form-label fw-semibold">Confirm Beneficiary Account Number</label>
                        <input type="text" class="form-control fw-bold font-monospace" id="confirmAccount" name="confirmAccount" 
                               placeholder="Re-enter beneficiary account" required value="${prevTarget}">
                    </div>

                    <div class="mb-3">
                        <label for="amount" class="form-label fw-semibold">Transfer Amount (INR)</label>
                        <div class="input-group input-group-lg">
                            <span class="input-group-text fw-bold">₹</span>
                            <input type="number" class="form-control fw-bold fs-4" id="amount" name="amount" 
                                   placeholder="1000" min="100" max="100000" step="10" required value="${prevAmount}">
                        </div>
                    </div>

                    <div class="mb-4">
                        <label for="description" class="form-label fw-semibold">Transfer Note / Remarks (Optional)</label>
                        <input type="text" class="form-control" id="description" name="description" 
                               placeholder="e.g. Rent, College Fees, Project Share" maxlength="60">
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg shadow-sm">
                            <i class="bi bi-send-check me-1"></i> Authorize &amp; Transfer Funds
                        </button>
                        <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-outline-secondary">
                            Cancel &amp; Return to Menu
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
