<%@ page contentType="text/html;charset=UTF-8" language="java" isErrorPage="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="500 - Server Error | Apex Bank" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<div class="container py-5 my-auto text-center">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <h1 class="display-1 fw-bold text-danger">500</h1>
            <h4 class="fw-bold mb-3">Core Banking System Notice</h4>
            <p class="text-secondary mb-4">
                An internal exception occurred during transaction processing. For security, your session and ledger balances remain protected.
            </p>
            <div class="d-flex justify-content-center gap-2">
                <a href="${pageContext.request.contextPath}/atm/dashboard" class="btn btn-primary px-4">
                    <i class="bi bi-arrow-left me-1"></i> Return to Dashboard
                </a>
                <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-secondary px-4">
                    ATM Login
                </a>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../common/footer.jsp" />
