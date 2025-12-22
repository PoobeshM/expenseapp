package com.expense.expenseapp.service;

import com.expense.expenseapp.dto.ExpenseRequestDto;
import com.expense.expenseapp.dto.ExpenseResponseDto;
import com.expense.expenseapp.model.User;

import java.util.List;

public interface ExpenseService {

    ExpenseResponseDto createExpense(ExpenseRequestDto dto, User user);

    List<ExpenseResponseDto> getAllExpenses(User user);

    ExpenseResponseDto getExpenseById(Long id, User user);

    ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto dto, User user);

    void deleteExpense(Long id, User user);
}
