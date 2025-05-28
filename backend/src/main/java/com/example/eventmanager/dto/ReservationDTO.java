package com.example.eventmanager.dto;

import lombok.Data;

@Data
public class ReservationDTO {
    private Long id;
    private Long eventId;
    private String eventName;
    private String reservationDate;
}
