/**
 * Apex Banking Main Application Script
 * Theme toggle, notifications, and UX helpers
 */

// Theme Management
function toggleTheme() {
    const currentTheme = document.documentElement.getAttribute("data-theme") || "light";
    const newTheme = currentTheme === "dark" ? "light" : "dark";
    document.documentElement.setAttribute("data-theme", newTheme);
    localStorage.setItem("apex_theme", newTheme);
    updateThemeIcon(newTheme);
}

function updateThemeIcon(theme) {
    const icon = document.getElementById("themeIcon");
    if (icon) {
        icon.className = theme === "dark" ? "bi bi-sun-fill" : "bi bi-moon-stars-fill";
    }
}

// Format input card numbers automatically as XXXX-XXXX-XXXX-XXXX
function setupCardFormatting() {
    const cardInput = document.getElementById("cardNumber");
    if (cardInput) {
        cardInput.addEventListener("input", (e) => {
            let val = e.target.value.replace(/\D/g, "");
            val = val.substring(0, 16);
            let formatted = val.match(/.{1,4}/g)?.join(" ") || val;
            e.target.value = formatted;
        });
    }
}

// Auto dismiss alerts after 5 seconds
function setupAlertAutoDismiss() {
    setTimeout(() => {
        const alerts = document.querySelectorAll(".alert.alert-dismissible");
        alerts.forEach(alert => {
            alert.classList.remove("show");
            setTimeout(() => alert.remove(), 250);
        });
    }, 5000);
}

document.addEventListener("DOMContentLoaded", () => {
    // Load persisted theme
    const savedTheme = localStorage.getItem("apex_theme") || "light";
    document.documentElement.setAttribute("data-theme", savedTheme);
    updateThemeIcon(savedTheme);

    const themeToggleBtn = document.getElementById("themeToggleBtn");
    if (themeToggleBtn) {
        themeToggleBtn.addEventListener("click", toggleTheme);
    }

    setupCardFormatting();
    setupAlertAutoDismiss();
});
