<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Forgot ATM PIN - OTP Simulation" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-5">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="text-center mb-4">
                    <div class="stat-icon orange mx-auto mb-2">
                        <i class="bi bi-shield-lock-fill"></i>
                    </div>
                    <h4 class="fw-bold mb-1">Reset ATM PIN</h4>
                    <p class="text-secondary small">Secure OTP Verification &amp; Card Unlock Simulation</p>
                </div>

                <c:choose>
                    <%-- STEP 1: Request OTP --%>
                    <c:when test="${step ne 'VERIFY'}">
                        <form action="${pageContext.request.contextPath}/forgot-pin" method="POST">
                            <input type="hidden" name="action" value="send_otp">

                            <div class="mb-3">
                                <label for="cardNumber" class="form-label fw-semibold">16-Digit ATM Card Number</label>
                                <input type="text" class="form-control" id="cardNumber" name="cardNumber" 
                                       placeholder="4532789012345678" required autofocus>
                            </div>

                            <div class="mb-3">
                                <label for="phone" class="form-label fw-semibold">Registered Mobile Number</label>
                                <input type="tel" class="form-control" id="phone" name="phone" 
                                       placeholder="9876543210" maxlength="10" required>
                                <div class="form-text">e.g. 9876543210 for John Doe</div>
                            </div>

                            <div class="d-grid gap-2 pt-2">
                                <button type="submit" class="btn btn-warning fw-bold text-dark">
                                    <i class="bi bi-chat-text-fill me-1"></i> Send Verification OTP
                                </button>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary">
                                    Cancel &amp; Return to Login
                                </a>
                            </div>
                        </form>
                    </c:when>

                    <%-- STEP 2: Verify OTP and set New PIN --%>
                    <c:otherwise>
                        <c:if test="${not empty simulatedOtp}">
                            <div class="alert alert-warning text-center border-warning">
                                <i class="bi bi-phone-fill me-1"></i>
                                <strong>Simulated SMS OTP Code:</strong> 
                                <span class="badge bg-dark fs-5 tracking-widest px-3 py-1 ms-2"><c:out value="${simulatedOtp}" /></span>
                                <div class="small text-muted mt-1">(Delivered to registered phone: <c:out value="${maskedPhone}" />)</div>
                            </div>
                        </c:if>

                        <form action="${pageContext.request.contextPath}/forgot-pin" method="POST">
                            <input type="hidden" name="action" value="verify_and_reset">
                            <input type="hidden" name="cardNumber" value="${cardNumber}">
                            <input type="hidden" name="phone" value="${phone}">

                            <div class="mb-3">
                                <label for="otp" class="form-label fw-semibold">Enter 6-Digit OTP</label>
                                <input type="text" class="form-control form-control-lg text-center fw-bold fs-4" id="otp" name="otp" 
                                       placeholder="123456" maxlength="6" value="${simulatedOtp}" required autofocus>
                            </div>

                            <div class="row g-2 mb-3">
                                <div class="col-6">
                                    <label for="newPin" class="form-label fw-semibold">New 4-Digit PIN</label>
                                    <input type="password" class="form-control text-center fw-bold" id="newPin" name="newPin" 
                                           placeholder="••••" maxlength="4" pattern="\d{4}" required>
                                </div>
                                <div class="col-6">
                                    <label for="confirmPin" class="form-label fw-semibold">Confirm New PIN</label>
                                    <input type="password" class="form-control text-center fw-bold" id="confirmPin" name="confirmPin" 
                                           placeholder="••••" maxlength="4" pattern="\d{4}" required>
                                </div>
                            </div>

                            <div class="d-grid gap-2 pt-2">
                                <button type="submit" class="btn btn-success fw-bold">
                                    <i class="bi bi-check-lg me-1"></i> Reset PIN &amp; Unlock Card
                                </button>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary">
                                    Cancel
                                </a>
                            </div>
                        </form>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
