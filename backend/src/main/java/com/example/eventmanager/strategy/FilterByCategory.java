package com.example.eventmanager.strategy;

import com.example.eventmanager.model.Event;
import java.util.List;
import java.util.stream.Collectors;

public class FilterByCategory extends EventFilterStrategy {
    private final String category;

    public FilterByCategory(String category) {
        this.category = category;
    }

    @Override
    public List<Event> filter(List<Event> events) {
        return events.stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }
}