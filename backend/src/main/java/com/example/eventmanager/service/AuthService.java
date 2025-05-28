//package com.example.eventmanager.service;
//
//import com.example.eventmanager.dto.*;
//import com.example.eventmanager.model.User;
//import com.example.eventmanager.model.User.Role;
//import com.example.eventmanager.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.*;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class AuthService {
//
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//
//    public AuthResponse register(RegisterRequest request) {
//        var user = new User();
//        user.setUsername(request.getUsername());
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setRole(Role.USER);
//        userRepository.save(user);
//
//        var jwt = jwtService.generateToken(user);
//        return new AuthResponse(jwt);
//    }
//
//    public AuthResponse login(LoginRequest request) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
//        );
//
//        var user = userRepository.findByUsername(request.getUsername())
//                .orElseThrow();
//        var jwt = jwtService.generateToken(user);
//        return new AuthResponse(jwt);
//    }
//}
