<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Insert ATM Card - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-5">
            <jsp:include page="../common/messages.jsp" />

            <!-- Card Visual Representation -->
            <div class="card-debit-visual mb-4">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <span class="fw-bold tracking-wider fs-5">APEX BANK</span>
                    <span class="badge bg-warning text-dark px-2 py-1">EMV DEBIT</span>
                </div>
                <div class="chip-graphic mb-3"></div>
                <div class="fs-4 fw-mono mb-3 tracking-widest" id="cardVisualNumber">
                    <c:out value="${not empty prevCard ? prevCard : '•••• •••• •••• ••••'}" />
                </div>
                <div class="d-flex justify-content-between align-items-end">
                    <div>
                        <div class="small opacity-75" style="font-size: 0.7rem;">CARDHOLDER</div>
                        <div class="fw-semibold" style="font-size: 0.9rem;" id="cardVisualName">VALUED CUSTOMER</div>
                    </div>
                    <div>
                        <div class="small opacity-75" style="font-size: 0.7rem;">VALID THRU</div>
                        <div class="fw-semibold" style="font-size: 0.9rem;">12/29</div>
                    </div>
                    <span class="fw-bold fs-6 fst-italic">RuPay</span>
                </div>
            </div>

            <!-- Login Form Card -->
            <div class="card card-bank p-4">
                <div class="text-center mb-4">
                    <h4 class="fw-bold mb-1">ATM Card Authentication</h4>
                    <p class="text-secondary small">Please enter your 16-digit Debit Card Number and 4-digit PIN.</p>
                </div>

                <form action="${pageContext.request.contextPath}/login" method="POST" id="atmLoginForm">
                    <div class="mb-3">
                        <label for="cardNumber" class="form-label fw-semibold">
                            <i class="bi bi-credit-card-2-front text-primary me-1"></i> ATM Card Number
                        </label>
                        <input type="text" class="form-control form-control-lg fw-mono" id="cardNumber" name="cardNumber" 
                               placeholder="4532 7890 1234 5678" maxlength="19" required autofocus 
                               value="${prevCard}">
                        <div class="form-text">Enter 16 digits printed on the front of your card.</div>
                    </div>

                    <div class="mb-3">
                        <div class="d-flex justify-content-between align-items-center">
                            <label for="pin" class="form-label fw-semibold">
                                <i class="bi bi-shield-lock-fill text-primary me-1"></i> 4-Digit ATM PIN
                            </label>
                            <a href="${pageContext.request.contextPath}/forgot-pin" class="small text-decoration-none">
                                Forgot PIN?
                            </a>
                        </div>
                        <input type="password" class="form-control form-control-lg text-center fw-bold fs-4" id="pin" name="pin" 
                               placeholder="••••" maxlength="4" pattern="\d{4}" inputmode="numeric" required>
                        <div class="form-text text-danger" style="font-size: 0.75rem;">
                            <i class="bi bi-info-circle me-1"></i> Card will lock automatically after 3 incorrect attempts.
                        </div>
                    </div>

                    <div class="d-grid gap-2 pt-2">
                        <button type="submit" class="btn btn-primary btn-lg shadow-sm">
                            <i class="bi bi-check2-circle me-1"></i> Authenticate &amp; Access ATM
                        </button>
                    </div>
                </form>

                <!-- Quick Test Credentials Autofill -->
                <div class="mt-4 pt-3 border-top">
                    <small class="text-muted d-block text-center mb-2">1-Click Quick Login Cards (All Customers in Database):</small>
                    <div class="d-flex flex-wrap gap-2 justify-content-center">
                        <c:forEach var="item" items="${cardProfiles}">
                            <button type="button" class="btn btn-outline-primary btn-sm" 
                                    onclick="fillCard('${item.card.cardNumber}', '${item.card.pinPlain}', '${item.customer.fullName}')">
                                <i class="bi bi-person-fill me-1"></i> ${item.customer.firstName} (PIN: ${item.card.pinPlain} | ₹<c:out value="${item.account.balance}" />)
                            </button>
                        </c:forEach>
                    </div>
                </div>

                <div class="text-center mt-3">
                    <a href="${pageContext.request.contextPath}/atm/kiosk" class="text-decoration-none small text-secondary">
                        <i class="bi bi-display me-1"></i> Prefer the physical ATM Hardware view? Click here
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function fillCard(number, pin, name) {
    const cardInput = document.getElementById("cardNumber");
    const pinInput = document.getElementById("pin");
    cardInput.value = number.replace(/\D/g, '').match(/.{1,4}/g).join(' ');
    pinInput.value = pin;
    document.getElementById("cardVisualNumber").innerText = cardInput.value;
    document.getElementById("cardVisualName").innerText = name.toUpperCase();
}

document.getElementById("cardNumber")?.addEventListener("input", function(e) {
    let val = e.target.value;
    document.getElementById("cardVisualNumber").innerText = val || "•••• •••• •••• ••••";
});
</script>

<jsp:include page="../common/footer.jsp" />
