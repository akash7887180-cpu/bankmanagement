<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Cash Withdrawal - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="d-flex align-items-center mb-4 pb-2 border-bottom">
                    <div class="stat-icon blue me-3">
                        <i class="bi bi-cash-stack"></i>
                    </div>
                    <div>
                        <h4 class="fw-bold mb-0">ATM Cash Withdrawal</h4>
                        <small class="text-secondary">Dispense physical currency notes</small>
                    </div>
                </div>

                <c:if test="${not empty dailyLimitRemaining}">
                    <div class="alert alert-info py-2 px-3 small mb-3">
                        <i class="bi bi-info-circle-fill me-1"></i>
                        Daily limit remaining today: <strong><c:out value="${dailyLimitRemaining}" /></strong>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/atm/withdraw" method="POST">
                    <div class="mb-3">
                        <label for="amount" class="form-label fw-semibold">Enter Amount to Withdraw (INR)</label>
                        <div class="input-group input-group-lg">
                            <span class="input-group-text fw-bold">₹</span>
                            <input type="number" class="form-control fw-bold fs-4" id="amount" name="amount" 
                                   placeholder="2000" min="100" max="40000" step="100" required autofocus>
                        </div>
                        <div class="form-text">
                            Enter amount in multiples of ₹100 or ₹500 notes. (Maximum ₹40,000 per day).
                        </div>
                    </div>

                    <!-- Preset Amount Buttons -->
                    <div class="mb-4">
                        <small class="text-muted d-block mb-2">Preset Quick Amounts:</small>
                        <div class="d-flex flex-wrap gap-2">
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setWithdrawAmount(500)">₹500</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setWithdrawAmount(1000)">₹1,000</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setWithdrawAmount(2000)">₹2,000</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setWithdrawAmount(5000)">₹5,000</button>
                            <button type="button" class="btn btn-outline-secondary btn-sm" onclick="setWithdrawAmount(10000)">₹10,000</button>
                        </div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg shadow-sm">
                            <i class="bi bi-cash me-1"></i> Dispense Cash
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
function setWithdrawAmount(amt) {
    document.getElementById("amount").value = amt;
}
</script>

<jsp:include page="../common/footer.jsp" />
