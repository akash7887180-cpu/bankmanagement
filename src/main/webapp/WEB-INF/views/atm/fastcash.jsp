<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Fast Cash - Apex National Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="text-center mb-4">
                    <div class="stat-icon orange mx-auto mb-2">
                        <i class="bi bi-lightning-charge-fill"></i>
                    </div>
                    <h4 class="fw-bold mb-1">Fast Cash Dispense</h4>
                    <p class="text-secondary small">One-click predefined withdrawal without manual typing</p>
                </div>

                <c:if test="${not empty dailyLimitRemaining}">
                    <div class="alert alert-info py-2 px-3 small mb-4 text-center">
                        <i class="bi bi-shield-check me-1"></i> Available Daily Withdrawal Quota: <strong><c:out value="${dailyLimitRemaining}" /></strong>
                    </div>
                </c:if>

                <div class="row g-3 mb-4">
                    <div class="col-6">
                        <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                            <input type="hidden" name="amount" value="500">
                            <button type="submit" class="fastcash-btn w-100 shadow-sm">
                                <span>₹ 500</span>
                                <span class="sub text-muted">1 x ₹500 Note</span>
                            </button>
                        </form>
                    </div>

                    <div class="col-6">
                        <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                            <input type="hidden" name="amount" value="1000">
                            <button type="submit" class="fastcash-btn w-100 shadow-sm">
                                <span>₹ 1,000</span>
                                <span class="sub text-muted">2 x ₹500 Notes</span>
                            </button>
                        </form>
                    </div>

                    <div class="col-6">
                        <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                            <input type="hidden" name="amount" value="2000">
                            <button type="submit" class="fastcash-btn w-100 shadow-sm">
                                <span>₹ 2,000</span>
                                <span class="sub text-muted">4 x ₹500 Notes</span>
                            </button>
                        </form>
                    </div>

                    <div class="col-6">
                        <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                            <input type="hidden" name="amount" value="5000">
                            <button type="submit" class="fastcash-btn w-100 shadow-sm">
                                <span>₹ 5,000</span>
                                <span class="sub text-muted">10 x ₹500 Notes</span>
                            </button>
                        </form>
                    </div>

                    <div class="col-12">
                        <form action="${pageContext.request.contextPath}/atm/fastcash" method="POST">
                            <input type="hidden" name="amount" value="10000">
                            <button type="submit" class="fastcash-btn w-100 shadow-sm border-warning">
                                <span>₹ 10,000</span>
                                <span class="sub text-muted">20 x ₹500 Notes</span>
                            </button>
                        </form>
                    </div>
                </div>

                <div class="text-center">
                    <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-outline-secondary w-100">
                        Cancel &amp; Return to Dashboard
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
