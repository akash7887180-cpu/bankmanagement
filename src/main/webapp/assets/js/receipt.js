/**
 * Apex ATM Receipt Handling Script
 * Printing and sharing functionality
 */

function printReceipt() {
    window.print();
}

function shareReceipt(details) {
    if (navigator.share) {
        navigator.share({
            title: "Apex National Bank - ATM Receipt",
            text: details || "Apex ATM Transaction Advice",
            url: window.location.href
        }).catch(() => {});
    } else {
        // Fallback to Clipboard
        navigator.clipboard.writeText(details || "Apex ATM Receipt - " + window.location.href)
            .then(() => alert("Receipt summary copied to clipboard!"))
            .catch(() => alert("Could not access clipboard."));
    }
}
