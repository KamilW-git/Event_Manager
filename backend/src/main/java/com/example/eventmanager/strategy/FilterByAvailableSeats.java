package com.example.eventmanager.strategy;

import com.example.eventmanager.model.Event;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByAvailableSeats extends EventFilterStrategy {

    @Override
    public List<Event> filter(List<Event> events) {
        return events.stream()
                .filter(e -> e.getSeatsTaken() < e.getSeatsTotal())
                .collect(Collectors.toList());
    }
}