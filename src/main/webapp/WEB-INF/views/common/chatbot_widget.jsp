<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Floating Chatbot Widget -->
<style>
#apexChatbotButton {
    position: fixed;
    bottom: 24px;
    right: 24px;
    width: 60px;
    height: 60px;
    border-radius: 50%;
    background: linear-gradient(135deg, #2563eb, #1d4ed8);
    color: #ffffff;
    box-shadow: 0 8px 24px rgba(37, 99, 235, 0.45);
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.6rem;
    cursor: pointer;
    z-index: 1050;
    transition: transform 0.3s cubic-bezier(0.34, 1.56, 0.64, 1), box-shadow 0.3s ease;
    border: 2px solid rgba(255, 255, 255, 0.2);
}
#apexChatbotButton:hover {
    transform: scale(1.08) translateY(-2px);
    box-shadow: 0 12px 30px rgba(37, 99, 235, 0.6);
}
#apexChatbotPing {
    position: absolute;
    top: 2px;
    right: 2px;
    width: 14px;
    height: 14px;
    background: #22c55e;
    border-radius: 50%;
    border: 2px solid #0f172a;
}
#apexChatbotWindow {
    position: fixed;
    bottom: 96px;
    right: 24px;
    width: 380px;
    max-width: calc(100vw - 48px);
    height: 520px;
    max-height: calc(100vh - 120px);
    background: #0f172a;
    border: 1px solid #334155;
    border-radius: 16px;
    box-shadow: 0 16px 40px rgba(0, 0, 0, 0.5);
    display: none;
    flex-direction: column;
    z-index: 1050;
    overflow: hidden;
}
#apexChatbotWindow.active {
    display: flex;
}
.chat-widget-header {
    background: linear-gradient(135deg, #1e3a8a, #0f172a);
    padding: 12px 16px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid #334155;
}
.chat-widget-body {
    flex: 1;
    overflow-y: auto;
    padding: 12px;
    display: flex;
    flex-direction: column;
    gap: 10px;
    background: #0b1120;
}
.chat-widget-bubble {
    max-width: 85%;
    padding: 8px 12px;
    border-radius: 12px;
    font-size: 0.85rem;
    line-height: 1.45;
}
.chat-widget-bubble.bot {
    align-self: flex-start;
    background: #1e293b;
    color: #f1f5f9;
    border: 1px solid #334155;
    border-bottom-left-radius: 2px;
}
.chat-widget-bubble.user {
    align-self: flex-end;
    background: #2563eb;
    color: #ffffff;
    border-bottom-right-radius: 2px;
}
.widget-pill {
    background: #1e293b;
    border: 1px solid #3b82f6;
    color: #93c5fd;
    padding: 2px 8px;
    border-radius: 12px;
    font-size: 0.75rem;
    cursor: pointer;
    white-space: nowrap;
}
.widget-pill:hover {
    background: #3b82f6;
    color: #ffffff;
}
</style>

<div id="apexChatbotButton" onclick="toggleChatWidget()" title="Chat with Apex Customer Service AI">
    <i class="bi bi-chat-dots-fill" id="chatBtnIcon"></i>
    <span id="apexChatbotPing"></span>
</div>

<div id="apexChatbotWindow">
    <div class="chat-widget-header">
        <div class="d-flex align-items-center gap-2">
            <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center" style="width: 32px; height: 32px;">
                <i class="bi bi-robot"></i>
            </div>
            <div>
                <strong class="text-light d-block" style="font-size: 0.9rem;">Apex HelpBot</strong>
                <small class="text-success" style="font-size: 0.72rem;"><i class="bi bi-circle-fill me-1" style="font-size: 0.5rem;"></i>24x7 Customer Care</small>
            </div>
        </div>
        <div>
            <a href="${pageContext.request.contextPath}/atm/chat" class="btn btn-sm btn-link text-light p-1" title="Open Fullscreen View">
                <i class="bi bi-arrows-angle-expand"></i>
            </a>
            <button class="btn btn-sm btn-link text-light p-1" onclick="toggleChatWidget()" title="Close">
                <i class="bi bi-x-lg"></i>
            </button>
        </div>
    </div>

    <!-- Messages -->
    <div class="chat-widget-body" id="widgetChatArea">
        <div class="chat-widget-bubble bot">
            👋 Hello <strong><c:out value="${sessionScope.SESSION_CUSTOMER.firstName}" default="Customer" /></strong>! I can assist with:
            <div class="mt-1">
                • <strong>Money Loans &amp; EMI</strong><br>
                • <strong>Instant Gold Loans</strong><br>
                • <strong>ATM Cash Disputes &amp; Lost Cards</strong>
            </div>
        </div>
    </div>

    <!-- Quick Action Chips -->
    <div class="p-2 border-top border-secondary bg-dark d-flex gap-1 overflow-x-auto" id="widgetSuggestions">
        <span class="widget-pill" onclick="sendWidgetMsg('💰 Money Loan EMI')">💰 Loans</span>
        <span class="widget-pill" onclick="sendWidgetMsg('🪙 Gold Loan')">🪙 Gold Loan</span>
        <span class="widget-pill" onclick="sendWidgetMsg('⚠️ ATM Cash Failed')">⚠️ ATM Issue</span>
        <span class="widget-pill" onclick="sendWidgetMsg('🚨 Block Card')">🚨 Block Card</span>
        <span class="widget-pill" onclick="sendWidgetMsg('💳 Check Balance')">💳 Balance</span>
    </div>

    <!-- Input -->
    <div class="p-2 border-top border-secondary bg-dark">
        <form onsubmit="handleWidgetSubmit(event)">
            <div class="input-group input-group-sm">
                <input type="text" class="form-control bg-dark border-secondary text-light" id="widgetInput" placeholder="Ask about loans or issues..." autocomplete="off" required>
                <button class="btn btn-primary" type="submit">
                    <i class="bi bi-send-fill"></i>
                </button>
            </div>
        </form>
    </div>
</div>

<script>
function toggleChatWidget() {
    const win = document.getElementById("apexChatbotWindow");
    const icon = document.getElementById("chatBtnIcon");
    win.classList.toggle("active");
    if (win.classList.contains("active")) {
        icon.className = "bi bi-chevron-down";
        document.getElementById("widgetInput").focus();
    } else {
        icon.className = "bi bi-chat-dots-fill";
    }
}

function handleWidgetSubmit(e) {
    if (e) e.preventDefault();
    const input = document.getElementById("widgetInput");
    const text = input.value.trim();
    if (!text) return;

    appendWidgetMsg(text, "user");
    input.value = "";

    fetch("${pageContext.request.contextPath}/api/chatbot", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ message: text })
    })
    .then(r => r.json())
    .then(data => {
        let msg = data.message;
        if (data.actionUrl && data.actionText) {
            msg += `<div class="mt-2"><a href="${pageContext.request.contextPath}` + data.actionUrl + `" class="btn btn-xs btn-primary text-white" style="font-size:0.75rem; padding: 2px 8px;">` + data.actionText + `</a></div>`;
        }
        appendWidgetMsg(msg, "bot");
    })
    .catch(() => {
        appendWidgetMsg("⚠️ Unable to reach customer service. Please visit our <a href='${pageContext.request.contextPath}/atm/support' class='text-warning'>Helpdesk</a>.", "bot");
    });
}

function sendWidgetMsg(text) {
    document.getElementById("widgetInput").value = text;
    handleWidgetSubmit();
}

function appendWidgetMsg(html, sender) {
    const area = document.getElementById("widgetChatArea");
    const div = document.createElement("div");
    div.className = "chat-widget-bubble " + sender;
    div.innerHTML = html;
    area.appendChild(div);
    area.scrollTop = area.scrollHeight;
}
</script>
