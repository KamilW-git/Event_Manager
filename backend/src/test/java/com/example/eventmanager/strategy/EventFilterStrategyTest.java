package com.example.eventmanager.strategy;

import com.example.eventmanager.model.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventFilterStrategyTest {

    private List<Event> events;

    @BeforeEach
    void setUp() {
        Event musicEvent = new Event();
        musicEvent.setId(1L);
        musicEvent.setName("Music Festival");
        musicEvent.setCategory("Music");
        musicEvent.setSeatsTotal(100);
        musicEvent.setSeatsTaken(50);
        musicEvent.setDate(LocalDate.from(LocalDateTime.now()));

        Event techEvent = new Event();
        techEvent.setId(2L);
        techEvent.setName("Tech Conference");
        techEvent.setCategory("Tech");
        techEvent.setSeatsTotal(100);
        techEvent.setSeatsTaken(100);
        techEvent.setDate(LocalDate.from(LocalDateTime.now()));

        events = List.of(musicEvent, techEvent);
    }

    @Test
    void filterByCategory_shouldReturnOnlyMatchingCategory() {
        FilterByCategory strategy = new FilterByCategory("Music");
        List<Event> filtered = strategy.filter(events);

        assertEquals(1, filtered.size());
        assertEquals("Music", filtered.get(0).getCategory());
    }

    @Test
    void filterByAvailableSeats_shouldReturnOnlyWithAvailableSeats() {
        FilterByAvailableSeats strategy = new FilterByAvailableSeats();
        List<Event> filtered = strategy.filter(events);

        assertEquals(1, filtered.size());
        assertTrue(filtered.get(0).getSeatsTaken() < filtered.get(0).getSeatsTotal());
    }

    @Test
    void eventFilterContext_shouldApplyCurrentStrategy() {
        EventFilterContext context = new EventFilterContext();
        context.setStrategy(new FilterByCategory("Tech"));

        List<Event> filtered = context.applyFilter(events);

        assertEquals(1, filtered.size());
        assertEquals("Tech", filtered.get(0).getCategory());
    }

    @Test
    void eventFilterContext_shouldThrowIfStrategyNotSet() {
        EventFilterContext context = new EventFilterContext();

        Exception ex = assertThrows(IllegalStateException.class, () -> {
            context.applyFilter(events);
        });

        assertEquals("Strategy not set", ex.getMessage());
    }
}

