package com.expense.expenseapp.controller;

import com.expense.expenseapp.dto.LoginRequest;
import com.expense.expenseapp.dto.RegisterRequest;
import com.expense.expenseapp.model.User;
import com.expense.expenseapp.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
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

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        userRepository.save(user);
        return "User registered";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request, HttpSession session) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        session.setAttribute("LOGGED_USER", user);
        return "Login successful";
    }

    @PostMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }
    @GetMapping("/me")
public User getLoggedInUser(HttpSession session) {
    return (User) session.getAttribute("LOGGED_USER");
}

}
