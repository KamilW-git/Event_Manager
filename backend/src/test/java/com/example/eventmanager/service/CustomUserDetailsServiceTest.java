package com.example.eventmanager.service;

import com.example.eventmanager.model.User;
import com.example.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomUserDetailsServiceTest {

    private UserRepository userRepo;
    private CustomUserDetailsService service;


    @BeforeEach
    void setUp() {
        userRepo = mock(UserRepository.class);
        service = new com.example.eventmanager.service.CustomUserDetailsService(userRepo);
    }

    @Test
    void shouldLoadUserByUsernameSuccessfully() {
        User user = new User();
        user.setUsername("john");
        user.setPassword("pass123");
        user.setRole(User.Role.USER);

        when(userRepo.findByUsername("john")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("john");

        assertEquals("john", details.getUsername());
        assertEquals("pass123", details.getPassword());
        assertTrue(details.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void shouldThrowIfUserNotFound() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("ghost");
        });
    }
}
