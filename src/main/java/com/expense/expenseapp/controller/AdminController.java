package com.expense.expenseapp.controller;
import com.expense.expenseapp.model.User;
import com.expense.expenseapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserRepository userRepository;
    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // ======================
    // GET ALL USERS (ADMIN)
    // ======================
    @GetMapping("/users")
    public List<User> getAllUsers(HttpSession session) {
        User admin = getLoggedInAdmin(session);
        return userRepository.findAll();
    }
    // ======================
    // UPDATE USERNAME (ADMIN)
    // ======================
    @PutMapping("/users/{id}")
    public User updateUsername(
            @PathVariable Long id,
            @RequestBody User updatedUser,
            HttpSession session
    ) {
        User admin = getLoggedInAdmin(session);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        user.setUsername(updatedUser.getUsername());
        return userRepository.save(user);
    }
    // ======================
    // DELETE USER (ADMIN)
    // ======================
    @DeleteMapping("/users/{id}")
    public void deleteUser(
            @PathVariable Long id,
            HttpSession session) {
        User admin = getLoggedInAdmin(session);
        if (admin.getId().equals(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Admin cannot delete self"
            );
        }
        userRepository.deleteById(id);
    }
    // ======================
    // HELPER: ADMIN SESSION CHECK
    // ======================
    private User getLoggedInAdmin(HttpSession session) {
        Long userId = (Long) session.getAttribute("LOGGED_USER_ID");
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        if (!"ADMIN".equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        return user;
    }
}
