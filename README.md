Expense Tracker Application
A full-stack Expense Tracker web application built using Spring Boot, PostgreSQL, and Vanilla JavaScript, featuring session-based authentication, role-based access (Admin/User), analytics dashboards, and filtering capabilities.
🚀 Features
👤 Authentication & Security
Session-based authentication using HttpSession
Passwords encrypted with BCrypt
Role-based access control (ADMIN / USER)
Secure REST APIs with Spring Security
No JWT (pure server-side session handling)
🧾 User Features
Register & Login
Add, view, and delete expenses
Filter expenses by:
Category
Date range
Month
View:
Total expenses
Current month expenses
Visual analytics:
Category distribution (Pie Chart)
Export expenses to CSV
🛠 Admin Features
View all users
Edit user usernames
Delete users (except self)
View expenses of any user
Month-wise filtering for user expenses
Admin analytics dashboard
🧑‍💻 Tech Stack
Backend
Java 17+
Spring Boot
Spring Security (SecurityFilterChain)
Spring Data JPA
PostgreSQL
Maven
Frontend
HTML5
CSS3 (Bootstrap)
Vanilla JavaScript
Chart.js
Database


PostgreSQLStructure of my Project
expenseapp/
├── src/main/java/com/expense/expenseapp
│   ├── config/        # Security configuration
│   ├── controller/    # REST controllers (Auth, Expense, Admin)
│   ├── dto/           # Request & response DTOs
│   ├── model/         # JPA entities
│   ├── repository/    # JPA repositories
│   ├── service/       # Business logic
│
├── src/main/resources
│   ├── static/
│   │   ├── login.html
│   │   ├── register.html
│   │   ├── index.html
│   │   ├── admin.html
│   │   ├── js/app.js
│   │   └── css/
│   └── application.properties
│
├── pom.xml
└── README.md
