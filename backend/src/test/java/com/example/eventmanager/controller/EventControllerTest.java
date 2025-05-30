package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventControllerTest {

    @Mock
    private EventRepository eventRepo;

    @InjectMocks
    private EventController eventController;

    private Event event;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDate(LocalDate.from(LocalDateTime.now()));
        event.setCategory("Music");
        event.setLocation("Krakow");
        event.setSeatsTotal(100);
    }

    @Test
    void shouldReturnAllEvents() {
        when(eventRepo.findAll()).thenReturn(List.of(event));

        List<Event> events = eventController.getAll();

        assertEquals(1, events.size());
        assertEquals("Test Event", events.get(0).getName());
    }

    @Test
    void shouldAddEvent() {
        when(eventRepo.save(any(Event.class))).thenReturn(event);

        Event saved = eventController.addEvent(event);

        assertEquals("Test Event", saved.getName());
        verify(eventRepo, times(1)).save(event);
    }

    @Test
    void shouldUpdateEvent() {
        Event updated = new Event();
        updated.setName("Updated Event");
        updated.setDate(LocalDate.from(LocalDateTime.now().plusDays(1)));
        updated.setCategory("Tech");
        updated.setLocation("Warsaw");
        updated.setSeatsTotal(200);

        when(eventRepo.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepo.save(any())).thenReturn(updated);

        Event result = eventController.updateEvent(1L, updated);

        assertEquals("Updated Event", result.getName());
        assertEquals("Tech", result.getCategory());
        assertEquals("Warsaw", result.getLocation());
    }

    @Test
    void shouldThrowWhenEventToUpdateNotFound() {
        when(eventRepo.findById(99L)).thenReturn(Optional.empty());

        Event updated = new Event();
        updated.setName("Test");

        assertThrows(ResponseStatusException.class, () -> {
            eventController.updateEvent(99L, updated);
        });
    }

    @Test
    void shouldDeleteEvent() {
        doNothing().when(eventRepo).deleteById(1L);

        eventController.deleteEvent(1L);

        verify(eventRepo, times(1)).deleteById(1L);
    }
}
