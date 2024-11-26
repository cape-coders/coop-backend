package com.capecoders.coop.events;


import java.util.ArrayList;
import java.util.List;

public class TestEventPublisher implements EventPublisher {
    private final List<Object> data = new ArrayList<>();
    @Override
    public void publish(Object event) {
        data.add(event);
    }

    public Boolean eventWasPublished(Class<?> eventClass) {
        return data.stream().anyMatch(event -> eventClass.isAssignableFrom(event.getClass()));
    }
}
