// ================= DOM =================
const title = document.getElementById("title");
const category = document.getElementById("category");
const amount = document.getElementById("amount");
const paymentMode = document.getElementById("paymentMode");
const notes = document.getElementById("notes");

const expenseTable = document.getElementById("expenseTable");
const totalAmount = document.getElementById("totalAmount");
const monthlyTotal = document.getElementById("monthlyTotal");

let allExpenses = [];
let expenseChart = null;

// ================= API HELPER =================
function apiFetch(url, options = {}) {
    return fetch(url, {
        ...options,
        credentials: "same-origin",
        headers: {
            "Content-Type": "application/json",
            ...(options.headers || {})
        }
    });
}

// ================= AUTH CHECK =================
async function checkLogin() {
    const res = await apiFetch("/api/auth/me");
    if (!res.ok) {
        window.location.href = "login.html";
        throw new Error("Not logged in");
    }
}

// ================= LOAD EXPENSES =================
async function loadExpenses() {
    const res = await apiFetch("/api/expenses");

    if (!res.ok) {
        alert("Session expired");
        window.location.href = "login.html";
        return;
    }

    allExpenses = await res.json();
    renderExpenses(allExpenses);
    updateTotals(allExpenses);
    renderChart(allExpenses);
}

// ================= RENDER TABLE =================
function renderExpenses(expenses) {
    expenseTable.innerHTML = "";

    if (expenses.length === 0) {
        expenseTable.innerHTML = `
            <tr>
                <td colspan="5" class="text-center">No expenses found</td>
            </tr>
        `;
        return;
    }

    expenses.forEach(e => {
        expenseTable.innerHTML += `
            <tr>
                <td>${e.title}</td>
                <td>${e.category}</td>
                <td>${e.amount}</td>
                <td>${e.paymentMode}</td>
                <td>
                    <button class="btn btn-danger btn-sm"
                        onclick="deleteExpense(${e.id})">
                        Delete
                    </button>
                </td>
            </tr>
        `;
    });
}

// ================= FILTER BY CATEGORY =================
function filterExpenses() {
    const selectedCategory =
        document.getElementById("filterCategory").value;

    const filtered = selectedCategory
        ? allExpenses.filter(e => e.category === selectedCategory)
        : allExpenses;

    renderExpenses(filtered);
    updateTotals(filtered);
    renderChart(filtered);
}

// ================= FILTER BY DATE =================
function filterByDate() {
    const fromInput = document.getElementById("fromDate").value;
    const toInput = document.getElementById("toDate").value;

    if (!fromInput || !toInput) {
        alert("Please select both dates");
        return;
    }

    const fromDate = new Date(fromInput + "T00:00:00");
    const toDate = new Date(toInput + "T23:59:59");

    const filtered = allExpenses.filter(e => {
        const created = new Date(e.createdAt);
        return created >= fromDate && created <= toDate;
    });

    renderExpenses(filtered);
    updateTotals(filtered);
    renderChart(filtered);
}

// ================= FILTER BY MONTH =================
function filterByMonth() {
    const selectedMonth =
        document.getElementById("monthSelect").value;

    if (selectedMonth === "") {
        renderExpenses(allExpenses);
        updateTotals(allExpenses);
        renderChart(allExpenses);
        return;
    }

    const month = Number(selectedMonth);

    const filtered = allExpenses.filter(e => {
        const d = new Date(e.createdAt);
        return d.getMonth() === month;
    });

    renderExpenses(filtered);
    updateTotals(filtered);
    renderChart(filtered);
}

// ================= TOTALS =================
function updateTotals(expenses) {
    const total = expenses.reduce((sum, e) => sum + e.amount, 0);
    totalAmount.innerText = total;

    const now = new Date();

    const monthTotal = expenses
        .filter(e => {
            const d = new Date(e.createdAt);
            return d.getMonth() === now.getMonth()
                && d.getFullYear() === now.getFullYear();
        })
        .reduce((sum, e) => sum + e.amount, 0);

    monthlyTotal.innerText = monthTotal;
}

// ================= ADD EXPENSE =================
async function addExpense() {
    const expense = {
        title: title.value,
        category: category.value,
        amount: Number(amount.value),
        paymentMode: paymentMode.value,
        notes: notes.value
    };

    if (!expense.title || !expense.category || !expense.amount) {
        alert("Please fill required fields");
        return;
    }

    const res = await apiFetch("/api/expenses", {
        method: "POST",
        body: JSON.stringify(expense)
    });

    if (!res.ok) {
        alert("Failed to save expense");
        return;
    }

    clearInputs();
    loadExpenses();
}

// ================= DELETE =================
async function deleteExpense(id) {
    await apiFetch(`/api/expenses/${id}`, { method: "DELETE" });
    loadExpenses();
}

// ================= LOGOUT =================
async function logout() {
    await apiFetch("/api/auth/logout", { method: "POST" });
    window.location.href = "login.html";
}

// ================= UTIL =================
function clearInputs() {
    title.value = "";
    category.value = "";
    amount.value = "";
    paymentMode.value = "";
    notes.value = "";
}

// ================= DARK MODE =================
function toggleDarkMode() {
    document.body.classList.toggle("dark");
    localStorage.setItem(
        "darkMode",
        document.body.classList.contains("dark")
    );
}

if (localStorage.getItem("darkMode") === "true") {
    document.body.classList.add("dark");
}

// ================= EXPORT CSV =================
function exportExcel() {
    let csv = "Title,Category,Amount,Payment\n";

    document.querySelectorAll("#expenseTable tr").forEach(row => {
        const cols = row.querySelectorAll("td");
        if (cols.length) {
            csv += `${cols[0].innerText},${cols[1].innerText},${cols[2].innerText},${cols[3].innerText}\n`;
        }
    });

    const blob = new Blob([csv], { type: "text/csv" });
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "expenses.csv";
    a.click();
}

// ================= PIE CHART =================
function renderChart(expenses) {
    const ctx = document.getElementById("expenseChart");
    if (!ctx) return;

    const categoryTotals = {};

    expenses.forEach(e => {
        categoryTotals[e.category] =
            (categoryTotals[e.category] || 0) + e.amount;
    });

    if (expenseChart) {
        expenseChart.destroy();
    }

    expenseChart = new Chart(ctx, {
        type: "pie",
        data: {
            labels: Object.keys(categoryTotals),
            datasets: [{
                data: Object.values(categoryTotals),
                backgroundColor: [
                    "#ff6384",
                    "#36a2eb",
                    "#ffcd56",
                    "#4bc0c0",
                    "#9966ff",
                    "#ff9f40"
                ]
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { position: "bottom" }
            }
        }
    });
}

// ================= INIT =================
window.onload = async () => {
    await checkLogin();
    loadExpenses();
};
