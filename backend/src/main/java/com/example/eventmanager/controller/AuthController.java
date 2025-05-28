import com.example.eventmanager.dto.RegisterRequest;
import com.example.eventmanager.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//package com.example.eventmanager.controller;
//
//import com.example.eventmanager.dto.*;
//import com.example.eventmanager.service.AuthService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/auth")
//@RequiredArgsConstructor
//public class AuthController {
//
//    private final AuthService authService;
//
//    @PostMapping("/register")
//    public AuthResponse register(@RequestBody RegisterRequest request) {
//        return authService.register(request);
//    }
//
//    @PostMapping("/login")
//    public AuthResponse login(@RequestBody LoginRequest request) {
//        return authService.login(request);
//    }
//}
//@RestController
//@RequestMapping("/api/auth")
//public class AuthController {
//
//    private final UserRepository userRepo;
//    private final PasswordEncoder passwordEncoder;
//
//    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder) {
//        this.userRepo = userRepo;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @PostMapping("/register")
//    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
//        if (userRepo.existsByUsername(request.getUsername())) {
//            return ResponseEntity.badRequest().body("Username already taken");
//        }
//
//        User user = new User();
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(request.getRole() != null ? request.getRole() : User.Role.USER); // domyślnie USER
//
//        userRepo.save(user);
//        return ResponseEntity.ok("User registered");
//    }
//}
