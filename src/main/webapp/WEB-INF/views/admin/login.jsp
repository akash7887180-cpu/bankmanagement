<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Bank Administrator Login - Apex National Bank" />
<jsp:include page="../common/header.jsp" />

<nav class="navbar navbar-expand-lg navbar-dark navbar-bank">
    <div class="container">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="32" height="32" class="me-2">
            <span>APEX NATIONAL BANK</span>
            <span class="badge bg-danger ms-2">ADMIN CONSOLE</span>
        </a>
        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline-light btn-sm">
            <i class="bi bi-credit-card me-1"></i> Customer ATM Login
        </a>
    </div>
</nav>

<div class="container py-5 my-auto">
    <div class="row justify-content-center">
        <div class="col-md-7 col-lg-4">
            <jsp:include page="../common/messages.jsp" />

            <div class="card card-bank p-4">
                <div class="text-center mb-4">
                    <div class="stat-icon purple mx-auto mb-2">
                        <i class="bi bi-shield-lock-fill"></i>
                    </div>
                    <h4 class="fw-bold mb-1">Administrative Login</h4>
                    <p class="text-secondary small">Restricted access for bank managers and auditors</p>
                </div>

                <form action="${pageContext.request.contextPath}/admin/login" method="POST">
                    <div class="mb-3">
                        <label for="username" class="form-label fw-semibold">Admin Username</label>
                        <input type="text" class="form-control" id="username" name="username" 
                               placeholder="e.g. admin" required autofocus value="${prevUsername}">
                    </div>

                    <div class="mb-4">
                        <label for="password" class="form-label fw-semibold">Password</label>
                        <input type="password" class="form-control" id="password" name="password" 
                               placeholder="••••••••" required>
                    </div>

                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-danger btn-lg shadow-sm">
                            <i class="bi bi-box-arrow-in-right me-1"></i> Authenticate as Admin
                        </button>
                    </div>
                </form>

                <div class="mt-4 pt-3 border-top text-center">
                    <small class="text-muted d-block mb-2">Default Admin Credentials:</small>
                    <button type="button" class="btn btn-outline-secondary btn-sm" onclick="fillAdmin('admin', 'admin123')">
                        Auto-fill: <strong>admin / admin123</strong>
                    </button>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
function fillAdmin(u, p) {
    document.getElementById("username").value = u;
    document.getElementById("password").value = p;
}
</script>

<jsp:include page="../common/footer.jsp" />
