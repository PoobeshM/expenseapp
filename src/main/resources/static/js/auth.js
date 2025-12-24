function login() {
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            username: username,
            password: password
        })
    })
    .then(res => {
        if (!res.ok) throw new Error("Login failed");
        return res.text();
    })
    .then(() => {
        return fetch("/api/auth/me", { credentials: "include" });
    })
    .then(res => res.json())
    .then(user => {
        if (user.role === "ADMIN") {
            window.location.href = "admin.html";
        } else {
            window.location.href = "index.html";
        }
    })
    .catch(() => {
        document.getElementById("errorBox").classList.remove("d-none");
    });
}


function logout() {
    fetch("/api/auth/logout", { method: "POST" })
        .then(() => window.location.href = "login.html");
}


