<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Apex HelpBot - 24x7 Customer Service" />
<jsp:include page="../common/header.jsp" />
<jsp:include page="../common/navbar.jsp" />

<style>
.chat-container {
    height: 65vh;
    overflow-y: auto;
    padding: 1rem;
    display: flex;
    flex-direction: column;
    gap: 1rem;
}
.chat-bubble {
    max-width: 80%;
    padding: 0.85rem 1.15rem;
    border-radius: 1rem;
    position: relative;
    font-size: 0.95rem;
    line-height: 1.5;
}
.chat-bubble.bot {
    align-self: flex-start;
    background: #1e293b;
    border: 1px solid #334155;
    color: #f8fafc;
    border-bottom-left-radius: 0.2rem;
}
.chat-bubble.user {
    align-self: flex-end;
    background: #2563eb;
    color: #ffffff;
    border-bottom-right-radius: 0.2rem;
}
.chat-avatar {
    width: 32px;
    height: 32px;
    border-radius: 50%;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    font-size: 1rem;
}
.suggestion-pill {
    background: #1e293b;
    border: 1px solid #3b82f6;
    color: #60a5fa;
    padding: 0.35rem 0.85rem;
    border-radius: 2rem;
    font-size: 0.82rem;
    cursor: pointer;
    transition: all 0.2s ease;
    display: inline-block;
    user-select: none;
}
.suggestion-pill:hover {
    background: #3b82f6;
    color: #ffffff;
    transform: translateY(-1px);
}
.typing-dots span {
    display: inline-block;
    width: 6px;
    height: 6px;
    border-radius: 50%;
    background: #94a3b8;
    margin: 0 2px;
    animation: typing 1.4s infinite ease-in-out both;
}
.typing-dots span:nth-child(1) { animation-delay: -0.32s; }
.typing-dots span:nth-child(2) { animation-delay: -0.16s; }
@keyframes typing {
    0%, 80%, 100% { transform: scale(0); }
    40% { transform: scale(1); }
}
</style>

<div class="container py-4">
    <!-- Header -->
    <div class="d-flex flex-wrap justify-content-between align-items-center mb-3 pb-2 border-bottom">
        <div class="d-flex align-items-center gap-3">
            <div class="chat-avatar bg-primary text-white fs-4 p-3" style="width: 48px; height: 48px;">
                <i class="bi bi-robot"></i>
            </div>
            <div>
                <h4 class="fw-bold mb-0">Apex HelpBot <span class="badge bg-success ms-2 fs-6"><i class="bi bi-circle-fill me-1" style="font-size: 0.6rem;"></i>Online 24x7</span></h4>
                <small class="text-secondary">Intelligent Customer Service &amp; Financial Problem Solving Assistant</small>
            </div>
        </div>
        <div class="mt-2 mt-md-0 d-flex gap-2">
            <a href="${pageContext.request.contextPath}/atm/loans" class="btn btn-sm btn-outline-warning">
                <i class="bi bi-bank me-1"></i> Loans Center
            </a>
            <a href="${pageContext.request.contextPath}/atm/support" class="btn btn-sm btn-outline-danger">
                <i class="bi bi-life-preserver me-1"></i> Grievance Desk
            </a>
        </div>
    </div>

    <!-- Chat Card -->
    <div class="card card-bank overflow-hidden shadow-lg border-secondary">
        <!-- Messages Area -->
        <div class="chat-container" id="chatArea">
            <!-- Initial Welcome Message -->
            <div class="d-flex gap-2 align-items-start">
                <div class="chat-avatar bg-primary text-white flex-shrink-0">
                    <i class="bi bi-robot"></i>
                </div>
                <div>
                    <div class="chat-bubble bot">
                        Hello <strong><c:out value="${sessionScope.SESSION_CUSTOMER.firstName}" default="Valued Customer" /></strong>! 👋<br>
                        I am your dedicated <strong>Apex AI Customer Care Executive</strong>. I can solve banking problems, answer questions, and perform instant calculations:
                        <ul class="mt-2 mb-2 ps-3 small">
                            <li><strong>Money Loans:</strong> Calculate monthly EMI, compare personal/home/business loan rates.</li>
                            <li><strong>Gold Loans:</strong> Live gold rates, loan-to-value (75% LTV) and instant cash eligibility.</li>
                            <li><strong>Financial Issues:</strong> Cash not dispensed disputes, emergency card blocking, frozen accounts.</li>
                            <li><strong>Account Operations:</strong> Live balance check and recent transactions.</li>
                        </ul>
                        How may I assist you today?
                    </div>
                    <small class="text-muted ms-2" style="font-size: 0.7rem;">Apex HelpBot • Just now</small>
                </div>
            </div>
        </div>

        <!-- Quick Suggestions Carousel -->
        <div class="p-2 border-top border-secondary bg-dark d-flex gap-2 flex-wrap" id="suggestionsContainer">
            <span class="suggestion-pill" onclick="sendSuggestion('💰 Money Loan EMI')">💰 Money Loan EMI</span>
            <span class="suggestion-pill" onclick="sendSuggestion('🪙 Gold Loan Rates')">🪙 Gold Loan Rates</span>
            <span class="suggestion-pill" onclick="sendSuggestion('⚠️ ATM Cash Not Dispensed')">⚠️ ATM Cash Not Dispensed</span>
            <span class="suggestion-pill" onclick="sendSuggestion('🚨 Emergency Block Card')">🚨 Emergency Block Card</span>
            <span class="suggestion-pill" onclick="sendSuggestion('💳 Check Balance')">💳 Check Balance</span>
            <span class="suggestion-pill" onclick="sendSuggestion('Gold loan for 50 grams')">Gold loan for 50 grams</span>
            <span class="suggestion-pill" onclick="sendSuggestion('Calculate loan for 500000')">Calculate loan for 500000</span>
        </div>

        <!-- Input Form -->
        <div class="p-3 border-top border-secondary bg-dark">
            <form id="chatForm" onsubmit="handleUserMessage(event)">
                <div class="input-group">
                    <input type="text" class="form-control form-control-lg bg-dark border-secondary text-light" 
                           id="chatInput" placeholder="Describe your issue or ask about loans (e.g., 'ATM didn't give cash', 'Gold rate today')..." 
                           autocomplete="off" required>
                    <button class="btn btn-primary px-4 fw-bold" type="submit" id="sendBtn">
                        <i class="bi bi-send-fill me-1"></i> Send
                    </button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
function appendUserMessage(text) {
    const chatArea = document.getElementById("chatArea");
    const div = document.createElement("div");
    div.className = "d-flex justify-content-end mb-2";
    div.innerHTML = `
        <div>
            <div class="chat-bubble user">` + escapeHtml(text) + `</div>
            <div class="text-end text-muted me-2" style="font-size: 0.7rem;">You • Just now</div>
        </div>
    `;
    chatArea.appendChild(div);
    chatArea.scrollTop = chatArea.scrollHeight;
}

function appendBotMessage(data) {
    const chatArea = document.getElementById("chatArea");
    const div = document.createElement("div");
    div.className = "d-flex gap-2 align-items-start mb-2";

    let actionBtn = "";
    if (data.actionUrl && data.actionText) {
        actionBtn = `
            <div class="mt-2 pt-2 border-top border-secondary">
                <a href="${pageContext.request.contextPath}` + data.actionUrl + `" class="btn btn-sm ` + (data.actionClass || 'btn-primary') + ` fw-bold shadow-sm">
                    ` + data.actionText + ` <i class="bi bi-arrow-right ms-1"></i>
                </a>
            </div>
        `;
    }

    div.innerHTML = `
        <div class="chat-avatar bg-primary text-white flex-shrink-0">
            <i class="bi bi-robot"></i>
        </div>
        <div>
            <div class="chat-bubble bot">
                ` + data.message + `
                ` + actionBtn + `
            </div>
            <small class="text-muted ms-2" style="font-size: 0.7rem;">Apex HelpBot • Just now</small>
        </div>
    `;
    chatArea.appendChild(div);
    chatArea.scrollTop = chatArea.scrollHeight;

    // Update suggestions if any
    if (data.suggestions && data.suggestions.length > 0) {
        const sc = document.getElementById("suggestionsContainer");
        sc.innerHTML = "";
        data.suggestions.forEach(s => {
            const pill = document.createElement("span");
            pill.className = "suggestion-pill";
            pill.innerText = s;
            pill.onclick = () => sendSuggestion(s);
            sc.appendChild(pill);
        });
    }
}

function showTypingIndicator() {
    const chatArea = document.getElementById("chatArea");
    const div = document.createElement("div");
    div.id = "typingIndicator";
    div.className = "d-flex gap-2 align-items-start mb-2";
    div.innerHTML = `
        <div class="chat-avatar bg-primary text-white flex-shrink-0">
            <i class="bi bi-robot"></i>
        </div>
        <div class="chat-bubble bot py-2 px-3">
            <div class="typing-dots">
                <span></span><span></span><span></span>
            </div>
        </div>
    `;
    chatArea.appendChild(div);
    chatArea.scrollTop = chatArea.scrollHeight;
}

function removeTypingIndicator() {
    const ind = document.getElementById("typingIndicator");
    if (ind) ind.remove();
}

function handleUserMessage(e) {
    if (e) e.preventDefault();
    const input = document.getElementById("chatInput");
    const text = input.value.trim();
    if (!text) return;

    appendUserMessage(text);
    input.value = "";
    showTypingIndicator();

    fetch("${pageContext.request.contextPath}/api/chatbot", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ message: text })
    })
    .then(r => r.json())
    .then(data => {
        removeTypingIndicator();
        appendBotMessage(data);
    })
    .catch(err => {
        removeTypingIndicator();
        appendBotMessage({
            message: "⚠️ I am having trouble connecting to the customer service network. Please try again or visit our <a href='${pageContext.request.contextPath}/atm/support' class='text-warning'>Helpdesk Portal</a>.",
            suggestions: ["Call customer care", "File a ticket"]
        });
    });
}

function sendSuggestion(text) {
    document.getElementById("chatInput").value = text;
    handleUserMessage();
}

function escapeHtml(text) {
    const map = { '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#039;' };
    return text.replace(/[&<>"']/g, m => map[m]);
}
</script>

<jsp:include page="../common/footer.jsp" />
