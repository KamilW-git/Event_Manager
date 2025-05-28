package com.example.eventmanager.repository;

import com.example.eventmanager.model.Reservation;
import com.example.eventmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(User user);
}
