fetch("/api/expenses")
    .then(res => {
        if (res.status === 401 || res.status === 403) {
            window.location.href = "login.html";
        }
    });
function loadUserInfo() {
    fetch("/api/auth/me")
        .then(res => res.json())
        .then(user => {
            document.getElementById("loggedUser").innerText =
                "👤 Welcome, " + user.username;
        });
}


// ================= GLOBAL =================
let allExpenses = [];
let editingId = null;
let pieChart = null;
let barChart = null;

// ================= LOGIN CHECK =================
if (!localStorage.getItem("user")) {
    localStorage.setItem("user", "demo"); // simple demo login
}

// ================= LOAD =================
window.onload = fetchExpenses;

// ================= FETCH =================
function fetchExpenses() {
    fetch("/api/expenses")
        .then(res => res.json())
        .then(data => {
            allExpenses = data;
            renderExpenses(data);
            calculateTotal(data);
            calculateMonthlyTotal(data);
            updateCharts(data);
        });
}

// ================= ADD / UPDATE =================
function addExpense() {
    const expense = {
        title: title.value,
        category: category.value,
        amount: Number(amount.value),
        paymentMode: paymentMode.value,
        notes: notes.value
    };

    if (!expense.title || !expense.category || !expense.amount) {
        alert("Fill required fields");
        return;
    }

    const url = editingId ? `/api/expenses/${editingId}` : "/api/expenses";
    const method = editingId ? "PUT" : "POST";

    fetch(url, {
        method,
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(expense)
    }).then(() => {
        editingId = null;
        clearInputs();
        fetchExpenses();
    });
}

// ================= RENDER =================
function renderExpenses(expenses) {
    expenseTable.innerHTML = "";

    expenses.forEach(e => {
        expenseTable.innerHTML += `
            <tr>
                <td>${e.title}</td>
                <td>${e.category}</td>
                <td>₹${e.amount}</td>
                <td>${e.paymentMode}</td>
                <td>
                    <button class="btn btn-warning btn-sm" onclick="editExpense(${e.id})">Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="deleteExpense(${e.id})">Delete</button>
                </td>
            </tr>
        `;
    });
}

// ================= EDIT =================
function editExpense(id) {
    const e = allExpenses.find(x => x.id === id);
    title.value = e.title;
    category.value = e.category;
    amount.value = e.amount;
    paymentMode.value = e.paymentMode;
    notes.value = e.notes;
    editingId = id;
}

// ================= DELETE =================
function deleteExpense(id) {
    if (!confirm("Delete expense?")) return;
    fetch(`/api/expenses/${id}`, { method: "DELETE" })
        .then(fetchExpenses);
}

// ================= FILTERS =================
function filterExpenses() {
    const cat = filterCategory.value;
    const filtered = cat ? allExpenses.filter(e => e.category === cat) : allExpenses;
    renderExpenses(filtered);
    calculateTotal(filtered);
    calculateMonthlyTotal(filtered);
    updateCharts(filtered);
}

function filterByDate() {
    const from = new Date(fromDate.value);
    const to = new Date(toDate.value);

    const filtered = allExpenses.filter(e => {
        const d = new Date(e.createdAt);
        return d >= from && d <= to;
    });

    renderExpenses(filtered);
    calculateTotal(filtered);
    calculateMonthlyTotal(filtered);
    updateCharts(filtered);
}

// ================= TOTALS =================
function calculateTotal(expenses) {
    totalAmount.innerText = expenses.reduce((s, e) => s + e.amount, 0);
}

function calculateMonthlyTotal(expenses) {
    const now = new Date();
    monthlyTotal.innerText = expenses
        .filter(e => {
            const d = new Date(e.createdAt);
            return d.getMonth() === now.getMonth() && d.getFullYear() === now.getFullYear();
        })
        .reduce((s, e) => s + e.amount, 0);
}

// ================= CHARTS =================
function updateCharts(expenses) {
    const totals = {};
    expenses.forEach(e => totals[e.category] = (totals[e.category] || 0) + e.amount);

    const labels = Object.keys(totals);
    const values = Object.values(totals);

    if (pieChart) pieChart.destroy();
    if (barChart) barChart.destroy();

    pieChart = new Chart(categoryChart, {
        type: "pie",
        data: { labels, datasets: [{ data: values }] }
    });

    barChart = new Chart(barChartCanvas = barChart || document.getElementById("barChart"), {
        type: "bar",
        data: { labels, datasets: [{ label: "Expenses", data: values }] }
    });
}

// ================= EXPORT =================
function exportExcel() {
    let csv = "Title,Category,Amount,Payment\n";
    allExpenses.forEach(e => {
        csv += `${e.title},${e.category},${e.amount},${e.paymentMode}\n`;
    });

    const blob = new Blob([csv], { type: "text/csv" });
    const a = document.createElement("a");
    a.href = URL.createObjectURL(blob);
    a.download = "expenses.csv";
    a.click();
}

// ================= UTILS =================
function clearInputs() {
    title.value = category.value = amount.value = paymentMode.value = notes.value = "";
}

function logout() {
    localStorage.clear();
    location.reload();
}
window.onload = function () {
    loadUserInfo();
    fetchExpenses();
};
function toggleDarkMode() {
    document.body.classList.toggle("dark");
    localStorage.setItem(
        "darkMode",
        document.body.classList.contains("dark")
    );
}

// persist
if (localStorage.getItem("darkMode") === "true") {
    document.body.classList.add("dark");
}
