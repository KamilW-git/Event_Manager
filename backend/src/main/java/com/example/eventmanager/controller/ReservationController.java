package com.example.eventmanager.controller;


import com.example.eventmanager.model.*;
import com.example.eventmanager.repository.*;
//import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    @Autowired
    public ReservationController(ReservationRepository reservationRepository,
                                 UserRepository userRepository,
                                 EventRepository eventRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
    }

    @PostMapping("/{eventId}")
    public String makeReservation(@PathVariable Long eventId, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getSeatsTaken() >= event.getSeatsTotal()) {
            return "No more seats available.";
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEvent(event);
        reservationRepository.save(reservation);

        event.setSeatsTaken(event.getSeatsTaken() + 1);
        eventRepository.save(event);

        return "Reservation successful.";
    }

    @GetMapping("/my")
    public List<Reservation> getUserReservations(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return reservationRepository.findByUser(user);
    }

    // ADMIN: podgląd rezerwacji danego wydarzenia
    @GetMapping("/admin/event/{eventId}")
    public int getReservationCount(@PathVariable Long eventId) {
        return (int) reservationRepository.findAll().stream()
                .filter(r -> r.getEvent().getId().equals(eventId))
                .count();
    }
}
