package com.capecoders.coop.springevents;

import com.capecoders.coop.chat.ChatUsersRepo;
import com.capecoders.coop.events.EventPublisher;
import com.capecoders.coop.events.UserAddedEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class SpringEventsIntegrationTest {
    @Autowired
    private ChatUsersRepo chatUsersRepo;
    @Autowired
    private EventPublisher springEventPublisher;

    @Test
    public void shouldPublishEvents () {
        assertTrue(chatUsersRepo.findUserByUserId(2L).isEmpty());
        springEventPublisher.publish(new UserAddedEvent(2L, "testUserName"));
        assertTrue(chatUsersRepo.findUserByUserId(2L).isPresent());
    }
}
