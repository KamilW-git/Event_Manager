package com.example.eventmanager.strategy;

import com.example.eventmanager.model.Event;
import java.util.List;

public class EventFilterContext {
    private EventFilterStrategy strategy;

    public void setStrategy(EventFilterStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Event> applyFilter(List<Event> events) {
        if (strategy == null) throw new IllegalStateException("Strategy not set");
        return strategy.filter(events);
    }
}