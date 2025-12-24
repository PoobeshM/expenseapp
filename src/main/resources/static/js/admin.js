// ================= INIT =================
window.onload = () => {
    loadUsers();
};

let monthlyBarChart = null;
let currentUserExpenses = [];
let selectedUserExpenses = [];

// ================= LOAD USERS =================
async function loadUsers() {

    const res = await fetch("/api/admin/users", {
        credentials: "same-origin"
    });

    if (!res.ok) {
        alert("Access denied");
        window.location.href = "login.html";
        return;
    }

    const users = await res.json();
    const list = document.getElementById("userList");
    list.innerHTML = "";

    users.forEach(user => {

        const li = document.createElement("li");
        li.className =
            "list-group-item d-flex justify-content-between align-items-center";

        li.innerHTML = `
            <span style="cursor:pointer"
                  onclick="loadUserExpenses(${user.id})">
                ${user.username} (${user.role})
            </span>
            <div>
                <button class="btn btn-sm btn-warning"
                        onclick="editUser(${user.id}, '${user.username}')">
                    Edit
                </button>
                <button class="btn btn-sm btn-danger"
                        onclick="deleteUser(${user.id})">
                    Delete
                </button>
            </div>
        `;

        list.appendChild(li);
    });
}

// ================= LOAD USER EXPENSES =================
async function loadUserExpenses(userId) {

    const res = await fetch(`/api/expenses/admin/user/${userId}`, {
        credentials: "same-origin"
    });

    currentUserExpenses = await res.json();
    selectedUserExpenses = currentUserExpenses;

    renderAdminExpenses(selectedUserExpenses);
    renderMonthlyBarChart(selectedUserExpenses);
}

// ================= RENDER TABLE =================
function renderAdminExpenses(expenses) {

    const table = document.getElementById("expenseTable");
    table.innerHTML = "";

    if (expenses.length === 0) {
        table.innerHTML = `
            <tr>
                <td colspan="4" class="text-center">No expenses</td>
            </tr>
        `;
        return;
    }

    expenses.forEach(e => {
        table.innerHTML += `
            <tr>
                <td>${e.title}</td>
                <td>${e.category}</td>
                <td>${e.amount}</td>
                <td>${e.paymentMode}</td>
            </tr>
        `;
    });
}

// ================= FILTER BY MONTH =================
function filterAdminByMonth() {

    const monthValue =
        document.getElementById("adminMonthSelect").value;

    if (monthValue === "") {
        selectedUserExpenses = currentUserExpenses;
    } else {
        const month = Number(monthValue);

        selectedUserExpenses = currentUserExpenses.filter(e => {
            const d = new Date(e.createdAt);
            return d.getMonth() === month;
        });
    }

    renderAdminExpenses(selectedUserExpenses);
    renderMonthlyBarChart(selectedUserExpenses);
}

// ================= BAR CHART =================
function renderMonthlyBarChart(expenses) {

    const ctx = document.getElementById("monthlyBarChart");
    if (!ctx) return;

    const monthlyTotals = new Array(12).fill(0);

    expenses.forEach(e => {
        const d = new Date(e.createdAt);
        monthlyTotals[d.getMonth()] += e.amount;
    });

    const labels = [
        "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    ];

    if (monthlyBarChart) {
        monthlyBarChart.destroy();
    }

    monthlyBarChart = new Chart(ctx, {
        type: "bar",
        data: {
            labels,
            datasets: [{
                label: "Expenses (₹)",
                data: monthlyTotals
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: { beginAtZero: true }
            }
        }
    });
}

// ================= EDIT USER =================
async function editUser(userId, oldName) {

    const newName = prompt("Enter new username", oldName);
    if (!newName) return;

    await fetch(`/api/admin/users/${userId}`, {
        method: "PUT",
        credentials: "same-origin",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username: newName })
    });

    loadUsers();
}

// ================= DELETE USER =================
async function deleteUser(userId) {

    if (!confirm("Delete this user and all their expenses?")) return;

    await fetch(`/api/admin/users/${userId}`, {
        method: "DELETE",
        credentials: "same-origin"
    });

    loadUsers();
    document.getElementById("expenseTable").innerHTML = "";
}

// ================= LOGOUT =================
async function logout() {
    await fetch("/api/auth/logout", {
        method: "POST",
        credentials: "same-origin"
    });
    window.location.href = "login.html";
}
