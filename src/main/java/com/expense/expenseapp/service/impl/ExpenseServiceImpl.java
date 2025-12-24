package com.expense.expenseapp.service.impl;

import com.expense.expenseapp.dto.ExpenseRequestDto;
import com.expense.expenseapp.dto.ExpenseResponseDto;
import com.expense.expenseapp.exception.ResourceNotFoundException;
import com.expense.expenseapp.model.Expense;
import com.expense.expenseapp.model.User;
import com.expense.expenseapp.repository.ExpenseRepository;
import com.expense.expenseapp.service.ExpenseService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseResponseDto createExpense(ExpenseRequestDto dto, User user) {

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        Expense expense = new Expense();
        expense.setTitle(dto.getTitle());
        expense.setCategory(dto.getCategory());
        expense.setAmount(dto.getAmount());
        expense.setPaymentMode(dto.getPaymentMode());
        expense.setNotes(dto.getNotes());
        expense.setUser(user);

        return mapToResponse(expenseRepository.save(expense));
    }

    @Override
    public List<ExpenseResponseDto> getAllExpenses(User user) {

        if (user == null) {
            throw new RuntimeException("User not logged in");
        }

        List<Expense> expenses;

        if ("ADMIN".equals(user.getRole())) {
            expenses = expenseRepository.findAll();
        } else {
            expenses = expenseRepository.findByUser(user);
        }

        return expenses.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponseDto getExpenseById(Long id, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!"ADMIN".equals(user.getRole())
                && (expense.getUser() == null
                || !expense.getUser().getId().equals(user.getId()))) {
            throw new RuntimeException("Unauthorized access");
        }

        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto dto, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!"ADMIN".equals(user.getRole())
                && (expense.getUser() == null
                || !expense.getUser().getId().equals(user.getId()))) {
            throw new RuntimeException("Unauthorized access");
        }

        expense.setTitle(dto.getTitle());
        expense.setCategory(dto.getCategory());
        expense.setAmount(dto.getAmount());
        expense.setPaymentMode(dto.getPaymentMode());
        expense.setNotes(dto.getNotes());

        return mapToResponse(expenseRepository.save(expense));
    }

    @Override
    public void deleteExpense(Long id, User user) {

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        if (!"ADMIN".equals(user.getRole())
                && (expense.getUser() == null
                || !expense.getUser().getId().equals(user.getId()))) {
            throw new RuntimeException("Unauthorized access");
        }

        expenseRepository.delete(expense);
    }

    private ExpenseResponseDto mapToResponse(Expense e) {

        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setCreatedAt(e.getCreatedAt());
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setCategory(e.getCategory());
        dto.setAmount(e.getAmount());
        dto.setPaymentMode(e.getPaymentMode());
        dto.setNotes(e.getNotes());

        return dto;
    }



}
