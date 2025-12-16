package com.expense.expenseapp.controller;

import com.expense.expenseapp.model.Expense;
import com.expense.expenseapp.repository.ExpenseRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test/expenses")
public class ExpenseTestController {

    private final ExpenseRepository expenseRepository;

    public ExpenseTestController(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    // Insert a sample expense
    @GetMapping("/add")
    public Expense addExpense() {
        Expense expense = new Expense();
        expense.setTitle("Lunch");
        expense.setCategory("Food");
        expense.setAmount(120.0);
        expense.setPaymentMode("UPI");
        expense.setNotes("Test expense");

        return expenseRepository.save(expense);
    }

    // Fetch all expenses
    @GetMapping("/all")
    public List<Expense> getAllExpenses() {
        return expenseRepository.findAll();
    }
}
