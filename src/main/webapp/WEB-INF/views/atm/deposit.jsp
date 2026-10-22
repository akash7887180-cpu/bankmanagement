<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Deposit Cash - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="d-flex align-items-center mb-4 pb-2 border-bottom">
                    <div class="stat-icon green me-3">
                        <i class="bi bi-wallet2"></i>
                    </div>
                    <div>
                        <h4 class="fw-bold mb-0">ATM Cash Deposit</h4>
                        <small class="text-secondary">Instant credit to primary ledger account</small>
                    </div>
                </div>

                <form action="${pageContext.request.contextPath}/atm/deposit" method="POST">
                    <div class="mb-3">
                        <label for="amount" class="form-label fw-semibold">Deposit Amount (INR)</label>
                        <div class="input-group input-group-lg">
                            <span class="input-group-text fw-bold">₹</span>
                            <input type="number" class="form-control fw-bold fs-4" id="amount" name="amount" 
                                   placeholder="5000" min="100" max="50000" step="100" required autofocus>
                        </div>
                        <div class="form-text">
                            Acceptable currency denominations: ₹100, ₹200, ₹500. Maximum per transaction: ₹50,000.
                        </div>
                    </div>

                    <!-- Preset Amount Buttons -->
                    <div class="mb-4">
                        <small class="text-muted d-block mb-2">Quick Select Amount:</small>
                        <div class="d-flex flex-wrap gap-2">
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDepositAmount(1000)">₹1,000</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDepositAmount(2500)">₹2,500</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDepositAmount(5000)">₹5,000</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDepositAmount(10000)">₹10,000</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setDepositAmount(20000)">₹20,000</button>
                        </div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-success btn-lg shadow-sm">
                            <i class="bi bi-check-circle-fill me-1"></i> Accept &amp; Deposit Cash
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

<script>
function setDepositAmount(amt) {
    document.getElementById("amount").value = amt;
}
</script>

<jsp:include page="../common/footer.jsp" />
