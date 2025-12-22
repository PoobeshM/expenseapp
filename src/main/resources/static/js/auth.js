function login() {
    const data = {
        username: document.getElementById("username").value,
        password: document.getElementById("password").value
    };

    fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) {
            document.getElementById("errorBox").classList.remove("d-none");
            throw new Error("Login failed");
        }
        return res.text();
    })
    .then(() => {
        // After successful login, get user details
        return fetch("/api/auth/me");
    })
    .then(res => res.json())
    .then(user => {
        // Role-based redirect
        if (user.role === "ADMIN") {
            window.location.href = "admin.html";
        } else {
            window.location.href = "index.html";
        }
    })
    .catch(() => {
        // error already shown in UI
    });
}
function logout() {
    fetch("/api/auth/logout", { method: "POST" })
        .then(() => window.location.href = "login.html");
}

