<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Manage Customers - Apex Admin" />
<jsp:include page="../common/header.jsp" />

<!-- Admin Top Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark border-bottom border-secondary px-3">
    <div class="container-fluid">
        <a class="navbar-brand d-flex align-items-center fw-bold" href="${pageContext.request.contextPath}/admin/dashboard">
            <img src="${pageContext.request.contextPath}/assets/img/bank-logo.svg" alt="Apex Logo" width="28" height="28" class="me-2">
            <span>APEX BANK ADMIN</span>
            <span class="badge bg-danger ms-2">CUSTOMERS</span>
        </a>
        <div class="d-flex align-items-center gap-2">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="btn btn-outline-light btn-sm">
                <i class="bi bi-speedometer2 me-1"></i> Dashboard
            </a>
            <a href="${pageContext.request.contextPath}/admin/logout" class="btn btn-outline-danger btn-sm">
                Logout
            </a>
        </div>
    </div>
</nav>

<div class="container-fluid px-4 py-4">
    <jsp:include page="../common/messages.jsp" />

    <div class="card card-bank p-4 mb-4">
        <div class="d-flex flex-wrap justify-content-between align-items-center mb-3">
            <div>
                <h4 class="fw-bold mb-1"><i class="bi bi-people-fill text-primary me-2"></i>Customer Directory</h4>
                <small class="text-secondary">Enrolled bank cardholders and account owners</small>
            </div>
            <div>
                <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addCustomerModal">
                    <i class="bi bi-person-plus-fill me-1"></i> Enrol New Customer
                </button>
            </div>
        </div>

        <div class="table-responsive">
            <table class="table table-bank table-hover align-middle mb-0">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Customer Name</th>
                        <th>Account &amp; Balance</th>
                        <th>ATM Debit Card</th>
                        <th>Contact / City</th>
                        <th class="text-center">Operate ATM</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${customers}">
                        <tr>
                            <td><span class="badge bg-secondary">#<c:out value="${c.customerId}" /></span></td>
                            <td>
                                <strong class="text-primary fs-6"><c:out value="${c.fullName}" /></strong>
                                <div class="small text-muted"><c:out value="${c.idProofType}" />: <code><c:out value="${c.idProofNumber}" /></code></div>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty accountMap[c.customerId]}">
                                        <div class="fw-bold font-monospace text-dark"><c:out value="${accountMap[c.customerId].accountNumber}" /></div>
                                        <span class="badge bg-success-subtle text-success border border-success">
                                            ₹<c:out value="${accountMap[c.customerId].balance}" /> (${accountMap[c.customerId].accountType})
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-outline-warning btn-sm py-0">
                                            + Open Account
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${not empty cardMap[c.customerId]}">
                                        <div class="fw-bold font-monospace text-primary fs-6"><c:out value="${cardMap[c.customerId].formattedCardNumber}" /></div>
                                        <div class="mt-1">
                                            <span class="badge bg-dark font-monospace px-2 py-1"><i class="bi bi-key-fill text-warning me-1"></i>PIN: <c:out value="${cardMap[c.customerId].pinPlain}" /></span>
                                            <span class="badge bg-secondary-subtle text-dark border ms-1"><c:out value="${cardMap[c.customerId].cardType.displayName}" /></span>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="text-muted small">No card issued</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <div><c:out value="${c.phone}" /></div>
                                <small class="text-muted"><c:out value="${c.city}" />, <c:out value="${c.state}" /></small>
                            </td>
                            <td class="text-center">
                                <c:choose>
                                    <c:when test="${not empty cardMap[c.customerId]}">
                                        <a href="${pageContext.request.contextPath}/login?quickCard=${cardMap[c.customerId].cardNumber}&quickPin=${cardMap[c.customerId].pinPlain}" 
                                           class="btn btn-success btn-sm shadow-sm fw-bold">
                                            <i class="bi bi-box-arrow-in-right me-1"></i> Launch ATM
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/admin/accounts" class="btn btn-warning btn-sm">
                                            Create Account First
                                        </a>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="text-center">
                                <a href="${pageContext.request.contextPath}/admin/customers?action=edit&id=${c.customerId}" 
                                   class="btn btn-outline-primary btn-sm me-1" title="Edit Customer">
                                    <i class="bi bi-pencil-square"></i>
                                </a>
                                <a href="${pageContext.request.contextPath}/admin/customers?action=delete&id=${c.customerId}" 
                                   class="btn btn-outline-danger btn-sm" 
                                   onclick="return confirm('Are you sure you want to delete customer: ${c.fullName}?');" 
                                   title="Delete Customer">
                                    <i class="bi bi-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Add Customer Modal -->
<div class="modal fade" id="addCustomerModal" tabindex="-1" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form action="${pageContext.request.contextPath}/admin/customers" method="POST">
                <input type="hidden" name="action" value="add">

                <div class="modal-header">
                    <h5 class="modal-title fw-bold"><i class="bi bi-person-plus-fill text-primary me-2"></i>Enrol New Customer</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>

                <div class="modal-body row g-3">
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">First Name</label>
                        <input type="text" class="form-control" name="firstName" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Last Name</label>
                        <input type="text" class="form-control" name="lastName" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Email Address</label>
                        <input type="email" class="form-control" name="email" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Phone Number</label>
                        <input type="tel" class="form-control" name="phone" maxlength="10" placeholder="10 digits" required>
                    </div>

                    <div class="col-md-4">
                        <label class="form-label fw-semibold">Date of Birth</label>
                        <input type="date" class="form-control" name="dob" required>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label fw-semibold">Gender</label>
                        <select class="form-select" name="gender">
                            <option value="MALE">Male</option>
                            <option value="FEMALE">Female</option>
                            <option value="OTHER">Other</option>
                        </select>
                    </div>
                    <div class="col-md-4">
                        <label class="form-label fw-semibold">ID Proof Type</label>
                        <select class="form-select" name="idProofType">
                            <option value="AADHAAR">Aadhaar Card</option>
                            <option value="PAN">PAN Card</option>
                            <option value="PASSPORT">Passport</option>
                        </select>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">ID Proof Number</label>
                        <input type="text" class="form-control" name="idProofNumber" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">City</label>
                        <input type="text" class="form-control" name="city" required>
                    </div>

                    <div class="col-md-6">
                        <label class="form-label fw-semibold">State</label>
                        <input type="text" class="form-control" name="state" required>
                    </div>
                    <div class="col-md-6">
                        <label class="form-label fw-semibold">Pincode</label>
                        <input type="text" class="form-control" name="pincode" required>
                    </div>

                    <div class="col-12">
                        <label class="form-label fw-semibold">Full Address</label>
                        <textarea class="form-control" name="address" rows="2" required></textarea>
                    </div>

                    <div class="col-12 mt-3 pt-3 border-top">
                        <div class="form-check form-switch mb-3">
                            <input class="form-check-input" type="checkbox" id="autoOpenAccountCheck" name="autoOpenAccount" value="true" checked onchange="toggleAccountFields(this.checked)">
                            <label class="form-check-label fw-bold text-primary" for="autoOpenAccountCheck">
                                <i class="bi bi-credit-card-2-front me-1"></i> Automatically Open Bank Account &amp; Issue ATM Card
                            </label>
                        </div>
                        <div id="accountFieldsRow" class="row g-3 p-3 bg-light rounded-3 border">
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold text-primary">
                                    <i class="bi bi-hash me-1"></i>Account Number <span class="text-muted fw-normal">(Optional - auto-generated if blank)</span>
                                </label>
                                <input type="text" class="form-control form-control-sm font-monospace fw-bold" name="accountNumber" placeholder="e.g. 1001007788" maxlength="20">
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold text-primary">
                                    <i class="bi bi-credit-card me-1"></i>ATM Card Number <span class="text-muted fw-normal">(Optional - auto-generated if blank)</span>
                                </label>
                                <input type="text" class="form-control form-control-sm font-monospace fw-bold" name="cardNumber" placeholder="e.g. 4532 9900 1122 3344" maxlength="19">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">Account Type</label>
                                <select class="form-select form-select-sm" name="accountType">
                                    <option value="SAVINGS" selected>Savings Account (Min ₹1,000)</option>
                                    <option value="CURRENT">Current Account (Min ₹5,000)</option>
                                </select>
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">Initial Deposit (₹)</label>
                                <input type="number" class="form-control form-control-sm fw-bold text-success" name="initialDeposit" value="5000" min="1000">
                            </div>
                            <div class="col-md-4">
                                <label class="form-label small fw-semibold">4-Digit ATM PIN</label>
                                <input type="text" class="form-control form-control-sm font-monospace fw-bold text-center border-primary" name="atmPin" value="1234" maxlength="4" pattern="\d{4}" required>
                                <small class="text-muted" style="font-size: 0.72rem;">Customer logs in with this PIN</small>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label small fw-semibold">ATM Card Network</label>
                                <select class="form-select form-select-sm" name="cardType">
                                    <option value="DEBIT_RUPAY" selected>RuPay Platinum Debit</option>
                                    <option value="DEBIT_VISA">Visa Classic Debit</option>
                                    <option value="DEBIT_MASTERCARD">MasterCard World Debit</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="submit" class="btn btn-success fw-bold">Register Customer &amp; Open Account</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function toggleAccountFields(show) {
    document.getElementById("accountFieldsRow").style.display = show ? "flex" : "none";
}
</script>

<jsp:include page="../common/footer.jsp" />
