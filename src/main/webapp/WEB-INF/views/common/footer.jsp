<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <!-- Global Footer -->
    <footer>
        <div class="container">
            <div class="row align-items-center g-3">
                <div class="col-md-6 text-center text-md-start">
                    <p class="mb-1 text-light fw-semibold">
                        Apex National Banking Corporation &copy; 2026. All rights reserved.
                    </p>
                    <small class="text-muted">
                        ATM Terminal: <strong>APEX-ATM-042</strong> • Connaught Place Financial Center, New Delhi
                    </small>
                </div>
                <div class="col-md-6 text-center text-md-end">
                    <small class="text-muted d-block">24x7 Customer Helpline: <strong class="text-light">1800-420-APEX (2739)</strong></small>
                    <small class="text-muted">Pure Java Servlets &bull; JSP &bull; JDBC &bull; MySQL &bull; MVC &bull; OOP</small>
                </div>
            </div>
        </div>
    </footer>

    <!-- Bootstrap 5 Bundle JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <!-- Custom Application JS -->
    <script src="${pageContext.request.contextPath}/assets/js/app.js"></script>
    <c:if test="${isReceiptPage}">
        <script src="${pageContext.request.contextPath}/assets/js/receipt.js"></script>
    </c:if>
    <c:if test="${isKioskPage}">
        <script src="${pageContext.request.contextPath}/assets/js/atm-kiosk.js"></script>
    </c:if>
</body>
</html>
