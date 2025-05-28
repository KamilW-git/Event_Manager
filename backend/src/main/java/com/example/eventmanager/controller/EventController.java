package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
//import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @GetMapping("/search/name")
    public List<Event> searchByName(@RequestParam String name) {
        return eventRepository.findByNameContainingIgnoreCase(name);
    }

    @GetMapping("/search/date")
    public List<Event> searchByDate(@RequestParam String date) {
        return eventRepository.findByDate(LocalDate.parse(date));
    }

    @GetMapping("/search/category")
    public List<Event> searchByCategory(@RequestParam String category) {
        return eventRepository.findByCategory(category);
    }

    // ADMIN
 @PostMapping("/admin")
    public Event addEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PutMapping("/admin/{id}")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updated) {
        return eventRepository.findById(id).map(event -> {
            event.setName(updated.getName());
            event.setDate(updated.getDate());
            event.setCategory(updated.getCategory());
            event.setLocation(updated.getLocation());
            event.setSeatsTotal(updated.getSeatsTotal());
            return eventRepository.save(event);
        }).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @DeleteMapping("/admin/{id}")
    public void deleteEvent(@PathVariable Long id) {
        eventRepository.deleteById(id);
    }
}
