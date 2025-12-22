package com.expense.expenseapp.repository;

import com.expense.expenseapp.model.Expense;
import com.expense.expenseapp.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);
}
