const expressionDisplay = document.getElementById("expressionDisplay");
const resultDisplay = document.getElementById("resultDisplay");
const historyList = document.getElementById("historyList");
const keypad = document.querySelector(".keypad");
const modeIndicator = document.querySelector(".history-header span");

let expression = "";
let justCalculated = false;
let currentMode = "backend";

keypad.addEventListener("click", (event) => {
    const button = event.target.closest("button");
    if (!button) {
        return;
    }

    const value = button.dataset.value;
    const action = button.dataset.action;

    if (value) {
        appendValue(value);
    } else if (action === "clear") {
        clearAll();
    } else if (action === "backspace") {
        backspace();
    } else if (action === "calculate") {
        calculate();
    }
});

document.addEventListener("keydown", (event) => {
    const key = event.key;

    if (/^[0-9.]$/.test(key) || ["+", "-", "*", "/"].includes(key)) {
        event.preventDefault();
        appendValue(key);
        flashKey(key);
    } else if (key === "Enter" || key === "=") {
        event.preventDefault();
        calculate();
        flashAction("calculate");
    } else if (key === "Backspace") {
        event.preventDefault();
        backspace();
        flashAction("backspace");
    } else if (key === "Escape" || key.toLowerCase() === "c") {
        event.preventDefault();
        clearAll();
        flashAction("clear");
    }
});

function appendValue(value) {
    resultDisplay.classList.remove("error");

    if (justCalculated && /[0-9.]/.test(value)) {
        expression = "";
    }

    justCalculated = false;

    if (isOperator(value)) {
        appendOperator(value);
    } else if (value === ".") {
        appendDecimalPoint();
    } else {
        expression += value;
    }

    updateDisplay();
}

function appendOperator(operator) {
    if (expression === "") {
        if (operator === "-") {
            expression = "-";
        }
        return;
    }

    const trimmed = expression.trimEnd();
    const lastCharacter = trimmed.at(-1);

    if (isOperator(lastCharacter)) {
        expression = `${trimmed.slice(0, -1)} ${operator} `;
    } else {
        expression = `${trimmed} ${operator} `;
    }
}

function appendDecimalPoint() {
    const currentNumber = expression.split(/[+\-*/]/).pop();
    if (!currentNumber.includes(".")) {
        expression += expression === "" || isOperator(expression.trimEnd().at(-1)) ? "0." : ".";
    }
}

function clearAll() {
    expression = "";
    justCalculated = false;
    resultDisplay.classList.remove("error");
    resultDisplay.textContent = "Ready";
    updateDisplay();
}

function backspace() {
    if (justCalculated) {
        justCalculated = false;
    }

    expression = expression.trimEnd().slice(0, -1).trimEnd();
    if (expression && isOperator(expression.at(-1))) {
        expression = `${expression} `;
    }
    resultDisplay.classList.remove("error");
    updateDisplay();
}

async function calculate() {
    const cleanExpression = expression.trim();

    if (!cleanExpression) {
        showError("Enter an expression");
        return;
    }

    resultDisplay.classList.remove("error");
    resultDisplay.textContent = "Calculating...";

    try {
        const response = await fetch("/api/calculate", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({ expression: cleanExpression })
        });

        const data = await response.json();
        if (!response.ok) {
            showError(data.message || "Invalid expression");
            return;
        }

        setMode("backend");
        expression = data.result;
        expressionDisplay.textContent = data.expression;
        resultDisplay.textContent = data.result;
        justCalculated = true;
        await loadHistory();
    } catch (error) {
        // Backend is unreachable. Fall back to frontend evaluator silently.
        setMode("offline");
        evaluateExpressionOffline(cleanExpression);
    }
}

async function loadHistory() {
    try {
        const response = await fetch("/api/history");
        if (!response.ok) {
            throw new Error("History unavailable");
        }

        const items = await response.json();
        setMode("backend");

        if (!items.length) {
            historyList.innerHTML = '<li class="empty">No calculations yet</li>';
            return;
        }

        historyList.innerHTML = items.map((item) => `
            <li>
                <div class="history-expression">${escapeHtml(item.expression)}</div>
                <div class="history-result">= ${escapeHtml(item.result)}</div>
                <div class="history-time">${new Date(item.timestamp).toLocaleString()}</div>
            </li>
        `).join("");
    } catch (error) {
        setMode("offline");
        historyList.innerHTML = '<li class="empty">History is available only in backend mode</li>';
    }
}

function updateDisplay() {
    expressionDisplay.textContent = expression || "0";
}

function setMode(mode) {
    currentMode = mode;

    if (!modeIndicator) {
        return;
    }

    if (mode === "backend") {
        modeIndicator.textContent = "Backend Mode";
        modeIndicator.classList.remove("offline-mode");
        modeIndicator.classList.add("backend-mode");
    } else {
        modeIndicator.textContent = "Offline Mode";
        modeIndicator.classList.remove("backend-mode");
        modeIndicator.classList.add("offline-mode");
    }
}

function evaluateExpressionOffline(expressionValue) {
    try {
        const result = evaluateExpressionFrontend(expressionValue);
        expression = String(result);
        expressionDisplay.textContent = expression;
        resultDisplay.textContent = String(result);
        justCalculated = true;
    } catch (error) {
        showError(error.message || "Invalid expression");
    }
}

function evaluateExpressionFrontend(expression) {
    const sanitized = sanitizeExpression(expression);
    if (!sanitized) {
        throw new Error("Invalid expression");
    }

    let result;
    try {
        result = new Function(`"use strict"; return (${sanitized});`)();
    } catch (error) {
        throw new Error("Invalid expression");
    }

    if (typeof result !== "number" || !Number.isFinite(result)) {
        throw new Error("Division by zero is not allowed");
    }

    return String(result);
}

function sanitizeExpression(expression) {
    const cleaned = expression.replaceAll(" ", "");
    const allowedPattern = /^[0-9+\-*/().]+$/;
    if (!allowedPattern.test(cleaned)) {
        return null;
    }

    // Disallow JavaScript-only syntax that the backend doesn't support
    if (cleaned.includes("**") || cleaned.includes("//") || cleaned.includes("/*") || cleaned.includes("*/")) {
        return null;
    }

    // Disallow empty parentheses or consecutive unmatched dots.
    if (/\(\)|\.{2,}|\(\.|\.\)/.test(cleaned)) {
        return null;
    }

    return cleaned;
}

function showError(message) {
    resultDisplay.classList.add("error");
    resultDisplay.textContent = message;
}

function isOperator(value) {
    return ["+", "-", "*", "/"].includes(value);
}

function flashKey(value) {
    const button = document.querySelector(`[data-value="${CSS.escape(value)}"]`);
    flashButton(button);
}

function flashAction(action) {
    const button = document.querySelector(`[data-action="${action}"]`);
    flashButton(button);
}

function flashButton(button) {
    if (!button) {
        return;
    }

    button.classList.add("active");
    window.setTimeout(() => button.classList.remove("active"), 120);
}

function escapeHtml(value) {
    return String(value)
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

loadHistory();
