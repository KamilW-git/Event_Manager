package com.example.eventmanager.controller;

import com.example.eventmanager.model.User;
import com.example.eventmanager.model.User.Role;
import com.example.eventmanager.repository.UserRepository;
import com.example.eventmanager.dto.RegisterRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            return ResponseEntity.badRequest().body("Username already taken");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : Role.USER);

        userRepo.save(user);
        return ResponseEntity.ok("User registered");
    }

    @GetMapping("/me")
    public User getCurrentUser(Authentication auth) {
        String username = auth.getName();
        System.out.println("🔍 Żądanie /me od: " + username);

        return userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(" Użytkownik nie znaleziony w bazie: " + username));
    }

}
