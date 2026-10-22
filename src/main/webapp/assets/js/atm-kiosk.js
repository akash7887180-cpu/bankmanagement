/**
 * Apex ATM Kiosk Interactive Hardware Script
 * Synthesizes realistic audio feedback via Web Audio API and manages keypad inputs
 */

class ATMAudio {
    constructor() {
        this.ctx = null;
    }

    init() {
        if (!this.ctx) {
            const AudioContext = window.AudioContext || window.webkitAudioContext;
            if (AudioContext) {
                this.ctx = new AudioContext();
            }
        }
    }

    // Keypad button click sound
    playKeyBeep(frequency = 1200) {
        this.init();
        if (!this.ctx) return;
        try {
            const osc = this.ctx.createOscillator();
            const gain = this.ctx.createGain();
            osc.type = "sine";
            osc.frequency.setValueAtTime(frequency, this.ctx.currentTime);
            gain.gain.setValueAtTime(0.12, this.ctx.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.001, this.ctx.currentTime + 0.08);
            osc.connect(gain);
            gain.connect(this.ctx.destination);
            osc.start();
            osc.stop(this.ctx.currentTime + 0.08);
        } catch (e) {
            // Audio context policy fallback
        }
    }

    // Cash dispenser motor churning sound
    playDispenseMotor() {
        this.init();
        if (!this.ctx) return;
        try {
            const osc = this.ctx.createOscillator();
            const gain = this.ctx.createGain();
            osc.type = "sawtooth";
            osc.frequency.setValueAtTime(120, this.ctx.currentTime);
            osc.frequency.exponentialRampToValueAtTime(180, this.ctx.currentTime + 0.8);
            gain.gain.setValueAtTime(0.15, this.ctx.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.001, this.ctx.currentTime + 1.2);
            osc.connect(gain);
            gain.connect(this.ctx.destination);
            osc.start();
            osc.stop(this.ctx.currentTime + 1.2);
        } catch (e) {}
    }

    // Card swipe sound
    playCardChime() {
        this.init();
        if (!this.ctx) return;
        try {
            const osc = this.ctx.createOscillator();
            const gain = this.ctx.createGain();
            osc.type = "triangle";
            osc.frequency.setValueAtTime(800, this.ctx.currentTime);
            osc.frequency.setValueAtTime(1400, this.ctx.currentTime + 0.1);
            gain.gain.setValueAtTime(0.1, this.ctx.currentTime);
            gain.gain.exponentialRampToValueAtTime(0.001, this.ctx.currentTime + 0.25);
            osc.connect(gain);
            gain.connect(this.ctx.destination);
            osc.start();
            osc.stop(this.ctx.currentTime + 0.25);
        } catch (e) {}
    }
}

const atmAudio = new ATMAudio();

document.addEventListener("DOMContentLoaded", () => {
    // Keypad integration
    const keys = document.querySelectorAll(".key-btn");
    let activeInput = document.querySelector("input[autofocus]") || document.querySelector("input[type='password']") || document.querySelector("input[type='number']") || document.querySelector("input[type='text']");

    // Track focused input
    document.querySelectorAll("input").forEach(input => {
        input.addEventListener("focus", () => {
            activeInput = input;
        });
    });

    keys.forEach(key => {
        key.addEventListener("click", () => {
            const val = key.getAttribute("data-val");
            const action = key.getAttribute("data-action");

            if (action === "cancel") {
                atmAudio.playKeyBeep(600);
                if (activeInput) activeInput.value = "";
                const cancelBtn = document.querySelector("#btnCancel");
                if (cancelBtn) cancelBtn.click();
            } else if (action === "clear") {
                atmAudio.playKeyBeep(900);
                if (activeInput) {
                    activeInput.value = activeInput.value.slice(0, -1);
                    activeInput.dispatchEvent(new Event("input"));
                }
            } else if (action === "enter") {
                atmAudio.playKeyBeep(1600);
                const form = (activeInput && activeInput.form) || document.querySelector("form");
                if (form) form.submit();
            } else if (val) {
                atmAudio.playKeyBeep(1100);
                if (activeInput) {
                    if (activeInput.maxLength && activeInput.value.length >= activeInput.maxLength) {
                        return;
                    }
                    activeInput.value += val;
                    activeInput.dispatchEvent(new Event("input"));
                }
            }
        });
    });

    // Card Insertion Demo click
    const cardSlot = document.querySelector(".slot-opening.card-slot");
    if (cardSlot) {
        cardSlot.addEventListener("click", () => {
            atmAudio.playCardChime();
            const cardInput = document.getElementById("cardNumber");
            if (cardInput && !cardInput.value) {
                cardInput.value = "4532789012345678";
                const pinInput = document.getElementById("pin");
                if (pinInput) pinInput.focus();
            }
        });
    }

    // Cash dispense trigger animation if on receipt page
    const dispenseTray = document.querySelector(".dispensed-notes");
    if (dispenseTray) {
        setTimeout(() => {
            atmAudio.playDispenseMotor();
            dispenseTray.classList.add("dispensing");
        }, 400);
    }
});
