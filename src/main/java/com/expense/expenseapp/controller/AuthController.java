package com.expense.expenseapp.controller;

import com.expense.expenseapp.dto.LoginRequest;
import com.expense.expenseapp.model.User;
import com.expense.expenseapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder;

    public AuthController(UserRepository userRepository,
                          BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    // ======================
    // REGISTER
    // ======================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    // ======================
    // LOGIN
    // ======================
   @PostMapping("/login")
public ResponseEntity<?> login(
        @RequestBody LoginRequest request,
        HttpSession session
) {

    User user = userRepository.findByUsername(request.getUsername())
            .orElse(null);

    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

    if (!encoder.matches(request.getPassword(), user.getPassword())) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Invalid username or password");
    }

    // ✅ Store user ID in session
    session.setAttribute("LOGGED_USER_ID", user.getId());

    return ResponseEntity.ok("Login successful");
}

    // ======================
    // LOGOUT
    // ======================
    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    // ======================
    // CURRENT USER
    // ======================
    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(HttpSession session) {

        Long userId = (Long) session.getAttribute("LOGGED_USER_ID");

        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Not logged in");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(user);
    }
}
