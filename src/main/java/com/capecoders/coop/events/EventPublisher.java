package com.capecoders.coop.events;

import org.springframework.context.ApplicationEventPublisher;

public interface EventPublisher {
    void publish(Object event);
}
