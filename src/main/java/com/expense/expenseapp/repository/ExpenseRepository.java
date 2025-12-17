package com.expense.expenseapp.repository;

import com.expense.expenseapp.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
