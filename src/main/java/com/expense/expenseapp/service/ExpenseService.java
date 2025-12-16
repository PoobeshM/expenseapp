package com.expense.expenseapp.service;

import com.expense.expenseapp.dto.ExpenseRequestDto;
import com.expense.expenseapp.dto.ExpenseResponseDto;

import java.util.List;

public interface ExpenseService {

    ExpenseResponseDto createExpense(ExpenseRequestDto requestDto);

    List<ExpenseResponseDto> getAllExpenses();

    ExpenseResponseDto getExpenseById(Long id);

    ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto requestDto);

    void deleteExpense(Long id);
}
