package com.example.eventmanager.controller;

import com.example.eventmanager.dto.RegisterRequest;
import com.example.eventmanager.model.User;
import com.example.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthControllerTest {

    @Mock
    private UserRepository userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldRegisterUserSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("newuser@example.com");
        request.setPassword("secret");
        request.setRole(User.Role.USER);

        when(userRepo.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("secret")).thenReturn("encodedPassword");
        when(userRepo.save(any(User.class))).thenReturn(new User());

        ResponseEntity<String> response = authController.register(request);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered", response.getBody());
    }

    @Test
    void shouldFailWhenUsernameTaken() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existinguser");

        when(userRepo.existsByUsername("existinguser")).thenReturn(true);

        ResponseEntity<String> response = authController.register(request);
        assertEquals(400, response.getStatusCodeValue());
    }
}
