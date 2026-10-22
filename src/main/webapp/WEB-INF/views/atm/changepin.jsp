<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Change ATM PIN - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="text-center mb-4 pb-2 border-bottom">
                    <div class="stat-icon orange mx-auto mb-2">
                        <i class="bi bi-key-fill"></i>
                    </div>
                    <h4 class="fw-bold mb-1">Change ATM PIN</h4>
                    <p class="text-secondary small">Set a new 4-digit security code for your ATM card</p>
                </div>

                <form action="${pageContext.request.contextPath}/atm/changepin" method="POST">
                    <div class="mb-3">
                        <label for="oldPin" class="form-label fw-semibold">Current ATM PIN</label>
                        <input type="password" class="form-control text-center fw-bold fs-5" id="oldPin" name="oldPin" 
                               placeholder="••••" maxlength="4" pattern="\d{4}" inputmode="numeric" required autofocus>
                    </div>

                    <div class="mb-3">
                        <label for="newPin" class="form-label fw-semibold">New 4-Digit PIN</label>
                        <input type="password" class="form-control text-center fw-bold fs-5" id="newPin" name="newPin" 
                               placeholder="••••" maxlength="4" pattern="\d{4}" inputmode="numeric" required>
                    </div>

                    <div class="mb-4">
                        <label for="confirmPin" class="form-label fw-semibold">Confirm New PIN</label>
                        <input type="password" class="form-control text-center fw-bold fs-5" id="confirmPin" name="confirmPin" 
                               placeholder="••••" maxlength="4" pattern="\d{4}" inputmode="numeric" required>
                        <div class="form-text">Choose a secret PIN that is difficult to guess. Never share your PIN.</div>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-warning fw-bold text-dark shadow-sm">
                            <i class="bi bi-shield-lock-fill me-1"></i> Update PIN Securely
                        </button>
                        <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-outline-secondary">
                            Cancel &amp; Return to Dashboard
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
