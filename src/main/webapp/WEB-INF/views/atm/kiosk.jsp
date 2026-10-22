<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="ATM Machine Kiosk Simulator - Apex National Bank" />
<c:set var="isKioskPage" value="true" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-4">
    <div class="d-flex justify-content-between align-items-center mb-3">
        <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-outline-secondary btn-sm">
            <i class="bi bi-arrow-left me-1"></i> Return to Modern Web Dashboard
        </a>
        <div class="badge bg-dark border border-secondary px-3 py-2">
            <i class="bi bi-soundwave text-warning me-1"></i> Interactive Web Audio Keypad Synthesizer Active
        </div>
    </div>

    <!-- Realistic Physical ATM Machine Casing -->
    <div class="atm-machine-container">
        <!-- Top Header Plate -->
        <div class="atm-header-plate">
            <div class="d-flex align-items-center">
                <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="36" height="36" class="me-3">
                <div>
                    <h5 class="fw-bold mb-0 tracking-wider">APEX NATIONAL BANK</h5>
                    <small class="text-info">AUTOMATED TELLER MACHINE • 24 HOUR SECURE NETWORK</small>
                </div>
            </div>
            <div class="atm-terminal-badge">
                ID: APEX-ATM-042
            </div>
        </div>

        <!-- Middle Screen Section with Physical Bezel Push-Buttons -->
        <div class="atm-screen-frame">
            <!-- Left Side Bezel Buttons -->
            <div class="atm-bezel-buttons">
                <button type="button" class="bezel-btn" title="Fast Cash ₹500" onclick="window.location.href='${pageContext.request.contextPath}/atm/fastcash'"></button>
                <button type="button" class="bezel-btn" title="Fast Cash ₹2000" onclick="window.location.href='${pageContext.request.contextPath}/atm/fastcash'"></button>
                <button type="button" class="bezel-btn" title="Balance Enquiry" onclick="window.location.href='${pageContext.request.contextPath}/atm/balance'"></button>
                <button type="button" class="bezel-btn" title="Mini Statement" onclick="window.location.href='${pageContext.request.contextPath}/atm/ministatement'"></button>
            </div>

            <!-- Green Phosphor CRT Screen Display -->
            <div class="atm-screen">
                <div class="atm-screen-content">
                    <div class="atm-screen-header">
                        <div class="atm-screen-title">APEX NATIONAL BANKING SYSTEM</div>
                        <div class="small">PLEASE SELECT YOUR TRANSACTION</div>
                    </div>

                    <c:choose>
                        <c:when test="${not empty sessionScope.SESSION_CARD}">
                            <div class="text-center my-2">
                                <div class="fs-6 fw-bold">CARDHOLDER: <c:out value="${customerName}" /></div>
                                <div class="small">CARD: <c:out value="${maskedCard}" /> • ACC: <c:out value="${maskedAccount}" /></div>
                                <div class="fs-5 text-warning mt-1">AVAILABLE BAL: <c:out value="${availableBalance}" /></div>
                            </div>

                            <div class="atm-screen-options">
                                <div class="screen-col">
                                    <a href="${pageContext.request.contextPath}/atm/fastcash" class="atm-option-tag">
                                        &lt; FAST CASH ₹500
                                    </a>
                                    <a href="${pageContext.request.contextPath}/atm/fastcash" class="atm-option-tag">
                                        &lt; FAST CASH ₹2000
                                    </a>
                                    <a href="${pageContext.request.contextPath}/atm/balance" class="atm-option-tag">
                                        &lt; BALANCE ENQUIRY
                                    </a>
                                    <a href="${pageContext.request.contextPath}/atm/ministatement" class="atm-option-tag">
                                        &lt; MINI STATEMENT
                                    </a>
                                </div>
                                <div class="screen-col right">
                                    <a href="${pageContext.request.contextPath}/atm/withdraw" class="atm-option-tag">
                                        CASH WITHDRAWAL &gt;
                                    </a>
                                    <a href="${pageContext.request.contextPath}/atm/deposit" class="atm-option-tag">
                                        CASH DEPOSIT &gt;
                                    </a>
                                    <a href="${pageContext.request.contextPath}/atm/transfer" class="atm-option-tag">
                                        MONEY TRANSFER &gt;
                                    </a>
                                    <a href="${pageContext.request.contextPath}/atm/changepin" class="atm-option-tag">
                                        CHANGE PIN &gt;
                                    </a>
                                </div>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="text-center my-auto">
                                <div class="fs-3 fw-bold mb-3 text-warning">WELCOME TO APEX ATM</div>
                                <p class="mb-4">PLEASE INSERT YOUR ATM DEBIT CARD TO COMMENCE TRANSACTIONS</p>
                                <a href="${pageContext.request.contextPath}/login" class="btn btn-success btn-lg px-4 font-monospace">
                                    <i class="bi bi-credit-card-2-front me-2"></i> INSERT CARD HERE
                                </a>
                            </div>
                        </c:otherwise>
                    </c:choose>

                    <div class="d-flex justify-content-between pt-2 border-top border-success small opacity-75">
                        <span>HELP: 1800-420-APEX</span>
                        <span>TERMINAL READY</span>
                    </div>
                </div>
            </div>

            <!-- Right Side Bezel Buttons -->
            <div class="atm-bezel-buttons">
                <button type="button" class="bezel-btn" title="Cash Withdrawal" onclick="window.location.href='${pageContext.request.contextPath}/atm/withdraw'"></button>
                <button type="button" class="bezel-btn" title="Cash Deposit" onclick="window.location.href='${pageContext.request.contextPath}/atm/deposit'"></button>
                <button type="button" class="bezel-btn" title="Money Transfer" onclick="window.location.href='${pageContext.request.contextPath}/atm/transfer'"></button>
                <button type="button" class="bezel-btn" title="Change PIN" onclick="window.location.href='${pageContext.request.contextPath}/atm/changepin'"></button>
            </div>
        </div>

        <!-- Lower Hardware Shelf: Card Slot, Cash Dispense Slot, and Metallic Keypad -->
        <div class="atm-hardware-shelf">
            <!-- Hardware Slots -->
            <div class="atm-slots-container">
                <!-- Card Reader Slot -->
                <div class="hardware-slot">
                    <div class="slot-opening card-slot" title="Click to simulate ATM Card insertion"></div>
                    <div class="slot-label">
                        <span><i class="bi bi-credit-card me-1"></i> Card Reader</span>
                        <span class="text-success"><i class="bi bi-circle-fill" style="font-size: 8px;"></i> Active</span>
                    </div>
                </div>

                <!-- Cash Dispenser Shutter Slot -->
                <div class="hardware-slot cash-dispense-tray">
                    <div class="slot-opening cash-slot" title="Cash Dispenser Shutter"></div>
                    <div class="slot-label">
                        <span><i class="bi bi-cash-stack me-1"></i> Cash Dispenser</span>
                        <span class="text-info">₹100 / ₹500 Notes</span>
                    </div>
                </div>
            </div>

            <!-- Metallic 3D Keypad with Audio Click Synthesizer -->
            <div class="atm-keypad" id="atmKeypad">
                <div class="key-btn key-num" data-val="1">1</div>
                <div class="key-btn key-num" data-val="2">2</div>
                <div class="key-btn key-num" data-val="3">3</div>
                <div class="key-btn key-cancel" data-action="cancel">CANCEL</div>

                <div class="key-btn key-num" data-val="4">4</div>
                <div class="key-btn key-num" data-val="5">5</div>
                <div class="key-btn key-num" data-val="6">6</div>
                <div class="key-btn key-clear" data-action="clear">CLEAR</div>

                <div class="key-btn key-num" data-val="7">7</div>
                <div class="key-btn key-num" data-val="8">8</div>
                <div class="key-btn key-num" data-val="9">9</div>
                <div class="key-btn key-enter" data-action="enter">ENTER</div>

                <div class="key-btn key-num" style="visibility: hidden;">.</div>
                <div class="key-btn key-num" data-val="0">0</div>
                <div class="key-btn key-num" data-val="00">00</div>
                <div class="key-btn key-num" style="visibility: hidden;">#</div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
