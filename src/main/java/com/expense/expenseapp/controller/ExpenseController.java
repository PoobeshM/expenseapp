package com.expense.expenseapp.controller;

import com.expense.expenseapp.dto.ExpenseRequestDto;
import com.expense.expenseapp.dto.ExpenseResponseDto;
import com.expense.expenseapp.model.User;
import com.expense.expenseapp.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin
public class ExpenseController {

    private final ExpenseService expenseService;
    private final HttpSession session;

    public ExpenseController(ExpenseService expenseService,
                             HttpSession session) {
        this.expenseService = expenseService;
        this.session = session;
    }

    // ✅ CREATE EXPENSE
    @PostMapping
    public ExpenseResponseDto createExpense(
            @RequestBody ExpenseRequestDto dto) {

        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        return expenseService.createExpense(dto, user);
    }

    // ✅ GET ALL EXPENSES
    @GetMapping
    public List<ExpenseResponseDto> getAllExpenses() {

        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        return expenseService.getAllExpenses(user);
    }

    // ✅ GET EXPENSE BY ID
    @GetMapping("/{id}")
    public ExpenseResponseDto getExpenseById(@PathVariable Long id) {

        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        return expenseService.getExpenseById(id, user);
    }

    // ✅ UPDATE EXPENSE
    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequestDto dto) {

        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        return expenseService.updateExpense(id, dto, user);
    }

    // ✅ DELETE EXPENSE
    @DeleteMapping("/{id}")
    public String deleteExpense(@PathVariable Long id) {

        User user = (User) session.getAttribute("LOGGED_USER");

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        expenseService.deleteExpense(id, user);
        return "Expense deleted successfully";
    }
}
