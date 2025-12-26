Structure of my Project
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
