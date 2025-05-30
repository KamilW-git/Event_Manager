package com.example.eventmanager.controller;

import com.example.eventmanager.dto.ReservationDTO;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.model.Reservation;
import com.example.eventmanager.model.User;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.repository.ReservationRepository;
import com.example.eventmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservationControllerTest {

    @Mock
    private ReservationRepository reservationRepo;

    @Mock
    private EventRepository eventRepo;

    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private ReservationController reservationController;

    private User user;
    private Event event;
    private Reservation reservation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setSeatsTotal(10);
        event.setSeatsTaken(5);

        reservation = new Reservation();
        reservation.setId(1L);
        reservation.setEvent(event);
        reservation.setUser(user);
        reservation.setReservationDate(LocalDateTime.now());
    }

    @Test
    void shouldCreateReservationSuccessfully() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(reservationRepo.countByUserIdAndEventId(1L, 1L)).thenReturn(0L);
        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservation);

        ReservationDTO dto = new ReservationDTO();
        dto.setEventId(1L);

        ResponseEntity<?> response = reservationController.createReservation(dto, "testuser");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Zarezerwowano", response.getBody());
    }

    @Test
    void shouldFailWhenNoSeatsAvailable() {
        event.setSeatsTaken(10);
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));

        ReservationDTO dto = new ReservationDTO();
        dto.setEventId(1L);

        ResponseEntity<?> response = reservationController.createReservation(dto, "testuser");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Brak dostępnych miejsc", response.getBody());
    }

    @Test
    void shouldFailWhenMaxReservationsReached() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(reservationRepo.countByUserIdAndEventId(1L, 1L)).thenReturn(2L);

        ReservationDTO dto = new ReservationDTO();
        dto.setEventId(1L);

        ResponseEntity<?> response = reservationController.createReservation(dto, "testuser");
        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Można zarezerwować maksymalnie 2 miejsca na wydarzenie", response.getBody());
    }

    @Test
    void shouldDeleteReservationAsOwner() {
        Authentication auth = getAuthentication("testuser", "ROLE_USER");
        when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));

        ResponseEntity<?> response = reservationController.delete(1L, auth);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldDeleteReservationAsAdmin() {
        Authentication auth = getAuthentication("admin", "ROLE_ADMIN");
        when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));

        ResponseEntity<?> response = reservationController.delete(1L, auth);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void shouldThrowWhenDeletingNotOwnerOrAdmin() {
        Authentication auth = getAuthentication("otheruser", "ROLE_USER");
        when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));

        assertThrows(ResponseStatusException.class, () -> {
            reservationController.delete(1L, auth);
        });
    }

    @Test
    void shouldThrowWhenDeletingNonExistingReservation() {
        Authentication auth = getAuthentication("testuser", "ROLE_USER");
        when(reservationRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            reservationController.delete(99L, auth);
        });
    }

    @Test
    void shouldUpdateReservationSuccessfully() {
        Reservation updated = new Reservation();
        updated.setReservationDate(LocalDateTime.now().plusDays(1));
        updated.setEvent(event);

        when(reservationRepo.findById(1L)).thenReturn(Optional.of(reservation));
        when(reservationRepo.save(any())).thenReturn(updated);

        Reservation result = reservationController.update(1L, updated);
        assertEquals(updated.getReservationDate(), result.getReservationDate());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingReservation() {
        Reservation updated = new Reservation();
        updated.setReservationDate(LocalDateTime.now());

        when(reservationRepo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> {
            reservationController.update(99L, updated);
        });
    }

    // 🔒 Utwórz ręczne Authentication bez mockowania
    private Authentication getAuthentication(String username, String role) {
        return new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return List.of(new SimpleGrantedAuthority(role));
            }

            @Override public Object getCredentials() { return null; }
            @Override public Object getDetails() { return null; }
            @Override public Object getPrincipal() { return null; }
            @Override public boolean isAuthenticated() { return true; }
            @Override public void setAuthenticated(boolean isAuthenticated) {}
            @Override public String getName() { return username; }
        };
    }
    @Test
    void shouldThrowWhenEventNotFound() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(eventRepo.findById(999L)).thenReturn(Optional.empty());

        Authentication auth = getAuthentication("testuser", "ROLE_USER");

        ReservationDTO dto = new ReservationDTO();
        dto.setEventId(999L);

        assertThrows(RuntimeException.class, () -> {
            reservationController.createReservation(dto, auth);
        });
    }
    @Test
    void shouldThrowWhenUserNotFound() {
        when(userRepo.findByUsername("ghost")).thenReturn(Optional.empty());

        Authentication auth = getAuthentication("ghost", "ROLE_USER");

        ReservationDTO dto = new ReservationDTO();
        dto.setEventId(1L);

        assertThrows(RuntimeException.class, () -> {
            reservationController.createReservation(dto, auth);
        });
    }

    @Test
    void shouldCreateReservationWithAuthentication() {
        when(userRepo.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(reservationRepo.countByUserIdAndEventId(user.getId(), event.getId())).thenReturn(0L);
        when(reservationRepo.save(any(Reservation.class))).thenReturn(reservation);

        Authentication auth = getAuthentication("testuser", "ROLE_USER");

        ReservationDTO dto = new ReservationDTO();
        dto.setEventId(1L);

        ResponseEntity<?> response = reservationController.createReservation(dto, auth);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Zarezerwowano", response.getBody());
    }



}
