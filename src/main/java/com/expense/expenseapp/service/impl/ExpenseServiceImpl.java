package com.expense.expenseapp.service.impl;

import com.expense.expenseapp.dto.ExpenseRequestDto;
import com.expense.expenseapp.dto.ExpenseResponseDto;
import com.expense.expenseapp.exception.ResourceNotFoundException;
import com.expense.expenseapp.model.Expense;
import com.expense.expenseapp.repository.ExpenseRepository;
import com.expense.expenseapp.service.ExpenseService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    private final ExpenseRepository expenseRepository;

    public ExpenseServiceImpl(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    public ExpenseResponseDto createExpense(ExpenseRequestDto requestDto) {
        Expense expense = new Expense();
        expense.setTitle(requestDto.getTitle());
        expense.setCategory(requestDto.getCategory());
        expense.setAmount(requestDto.getAmount());
        expense.setPaymentMode(requestDto.getPaymentMode());
        expense.setNotes(requestDto.getNotes());

        Expense savedExpense = expenseRepository.save(expense);
        return mapToResponse(savedExpense);
    }

    @Override
    public List<ExpenseResponseDto> getAllExpenses() {
        return expenseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ExpenseResponseDto getExpenseById(Long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found with id " + id)
                );
        return mapToResponse(expense);
    }

    @Override
    public ExpenseResponseDto updateExpense(Long id, ExpenseRequestDto requestDto) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Expense not found with id " + id)
                );

        expense.setTitle(requestDto.getTitle());
        expense.setCategory(requestDto.getCategory());
        expense.setAmount(requestDto.getAmount());
        expense.setPaymentMode(requestDto.getPaymentMode());
        expense.setNotes(requestDto.getNotes());

        Expense updatedExpense = expenseRepository.save(expense);
        return mapToResponse(updatedExpense);
    }

    @Override
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Expense not found with id " + id);
        }
        expenseRepository.deleteById(id);
    }

    private ExpenseResponseDto mapToResponse(Expense expense) {
        ExpenseResponseDto dto = new ExpenseResponseDto();
        dto.setId(expense.getId());
        dto.setTitle(expense.getTitle());
        dto.setCategory(expense.getCategory());
        dto.setAmount(expense.getAmount());
        dto.setPaymentMode(expense.getPaymentMode());
        dto.setNotes(expense.getNotes());
        dto.setCreatedAt(expense.getCreatedAt());
        return dto;
    }
}
