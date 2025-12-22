window.onload = loadAllExpenses;

function loadAllExpenses() {
    fetch("/api/expenses")
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById("adminExpenseTable");
            table.innerHTML = "";

            data.forEach(e => {
                table.innerHTML += `
                    <tr>
                        <td>${e.title}</td>
                        <td>${e.category}</td>
                        <td>₹${e.amount}</td>
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
        });
}

function deleteExpense(id) {
    if (!confirm("Delete this expense?")) return;

    fetch(`/api/expenses/${id}`, {
        method: "DELETE"
    }).then(loadAllExpenses);
}
function loadUsers() {
    fetch("/api/auth/users")
        .then(res => res.json())
        .then(data => {
            const table = document.getElementById("userTable");
            table.innerHTML = "";

            data.forEach(u => {
                table.innerHTML += `
                    <tr>
                        <td>${u.id}</td>
                        <td>${u.username}</td>
                        <td>${u.role}</td>
                    </tr>
                `;
            });
        });
}
window.onload = function () {
    loadAllExpenses();
    loadUsers();
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
