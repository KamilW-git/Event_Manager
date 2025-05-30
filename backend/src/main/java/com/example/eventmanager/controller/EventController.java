package com.example.eventmanager.controller;

import com.example.eventmanager.model.Event;
import com.example.eventmanager.repository.EventRepository;
import com.example.eventmanager.strategy.EventFilterContext;
import com.example.eventmanager.strategy.FilterByAvailableSeats;
import com.example.eventmanager.strategy.FilterByCategory;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@SecurityRequirement(name = "basicAuth")
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventRepository eventRepo;

    public EventController(EventRepository eventRepo) {
        this.eventRepo = eventRepo;
    }

    @GetMapping
    public List<Event> getAll() {
        return eventRepo.findAll();
    }

    @PostMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Event addEvent(@RequestBody Event event) {
        return eventRepo.save(event);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Event updateEvent(@PathVariable Long id, @RequestBody Event updated) {
        return eventRepo.findById(id)
                .map(e -> {
                    e.setName(updated.getName());
                    e.setDate(updated.getDate());
                    e.setCategory(updated.getCategory());
                    e.setLocation(updated.getLocation());
                    e.setSeatsTotal(updated.getSeatsTotal());
                    return eventRepo.save(e);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteEvent(@PathVariable Long id) {
        eventRepo.deleteById(id);
    }

    @GetMapping("/filter/by-category")
    public List<Event> filterByCategory(@RequestParam String category) {
        EventFilterContext ctx = new EventFilterContext();
        ctx.setStrategy(new FilterByCategory(category));
        return ctx.applyFilter(eventRepo.findAll());
    }

    @GetMapping("/filter/available")
    public List<Event> filterByAvailability() {
        EventFilterContext ctx = new EventFilterContext();
        ctx.setStrategy(new FilterByAvailableSeats());
        return ctx.applyFilter(eventRepo.findAll());
    }
}
