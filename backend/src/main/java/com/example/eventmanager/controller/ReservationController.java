package com.example.eventmanager.controller;

import com.example.eventmanager.dto.ReservationDTO;
import com.example.eventmanager.model.Reservation;
import com.example.eventmanager.model.User;
import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.ReservationRepository;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationRepository reservationRepo;
    private final EventRepository eventRepo;
    private final UserRepository userRepo;

    public ReservationController(ReservationRepository reservationRepo, EventRepository eventRepo, UserRepository userRepo) {
        this.reservationRepo = reservationRepo;
        this.eventRepo = eventRepo;
        this.userRepo = userRepo;
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth) {
        Reservation res = reservationRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        String current = auth.getName();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !res.getUser().getUsername().equals(current)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        reservationRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Reservation update(@PathVariable Long id, @RequestBody Reservation updated) {
        return reservationRepo.findById(id)
                .map(r -> {
                    r.setReservationDate(updated.getReservationDate());
                    r.setEvent(updated.getEvent());
                    return reservationRepo.save(r);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }


    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<?> createReservation(@RequestBody ReservationDTO dto, Authentication auth) {
        User user = userRepo.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Event event = eventRepo.findById(dto.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (event.getSeatsTaken() >= event.getSeatsTotal()) {
            return ResponseEntity.badRequest().body("Brak dostępnych miejsc");
        }

        long count = reservationRepo.countByUserIdAndEventId(user.getId(), event.getId());
        if (count >= 2) {
            return ResponseEntity.badRequest().body("Można zarezerwować maksymalnie 2 miejsca na wydarzenie");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setEvent(event);
        reservation.setReservationDate(LocalDateTime.now());

        reservationRepo.save(reservation);

        event.setSeatsTaken(event.getSeatsTaken() + 1);
        eventRepo.save(event);

        return ResponseEntity.ok("Zarezerwowano");
    }

}
