package com.example.eventmanager.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class EventDTO {
    private Long id;
    private String name;
    private LocalDate date;
    private String category;
    private String location;
    private int seatsTotal;
    private int seatsTaken;
}
