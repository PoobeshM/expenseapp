package com.expense.expenseapp.controller;

import com.expense.expenseapp.dto.ExpenseRequestDto;
import com.expense.expenseapp.dto.ExpenseResponseDto;
import com.expense.expenseapp.model.User;
import com.expense.expenseapp.repository.UserRepository;
import com.expense.expenseapp.service.ExpenseService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "http://localhost:8080", allowCredentials = "true")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserRepository userRepository;

    public ExpenseController(
            ExpenseService expenseService,
            UserRepository userRepository
    ) {
        this.expenseService = expenseService;
        this.userRepository = userRepository;
    }

    // ======================
    // CREATE EXPENSE
    // ======================
    @PostMapping
    public ExpenseResponseDto createExpense(
            @RequestBody ExpenseRequestDto dto,
            HttpSession session
    ) {
        User user = getLoggedInUser(session);
        return expenseService.createExpense(dto, user);
    }

    // ======================
    // GET ALL EXPENSES
    // ======================
    @GetMapping
    public List<ExpenseResponseDto> getAllExpenses(HttpSession session) {
        User user = getLoggedInUser(session);
        return expenseService.getAllExpenses(user);
    }

    // ======================
    // GET EXPENSE BY ID
    // ======================
    @GetMapping("/{id}")
    public ExpenseResponseDto getExpenseById(
            @PathVariable Long id,
            HttpSession session
    ) {
        User user = getLoggedInUser(session);
        return expenseService.getExpenseById(id, user);
    }

    // ======================
    // UPDATE EXPENSE
    // ======================
    @PutMapping("/{id}")
    public ExpenseResponseDto updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequestDto dto,
            HttpSession session
    ) {
        User user = getLoggedInUser(session);
        return expenseService.updateExpense(id, dto, user);
    }

    // ======================
    // DELETE EXPENSE
    // ======================
    @DeleteMapping("/{id}")
    public String deleteExpense(
            @PathVariable Long id,
            HttpSession session
    ) {
        User user = getLoggedInUser(session);
        expenseService.deleteExpense(id, user);
        return "Expense deleted successfully";
    }

    // ======================
    // SESSION HELPER (IMPORTANT)
    // ======================
    private User getLoggedInUser(HttpSession session) {

        Long userId = (Long) session.getAttribute("LOGGED_USER_ID");

        if (userId == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "User not logged in"
            );
        }

        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED,
                        "User not found"
                ));
    }


    // ======================
// ADMIN: VIEW ALL EXPENSES
// ======================




@GetMapping("/admin/user/{userId}")
public List<ExpenseResponseDto> getExpensesByUserId(
        @PathVariable Long userId,
        HttpSession session
) {

    User admin = getLoggedInUser(session);

    if (!"ADMIN".equals(admin.getRole())) {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    User targetUser = userRepository.findById(userId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return expenseService.getAllExpenses(targetUser);
}

}
