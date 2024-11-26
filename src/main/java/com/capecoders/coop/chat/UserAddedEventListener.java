package com.capecoders.coop.chat;

import com.capecoders.coop.events.UserAddedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class UserAddedEventListener {
    private final ChatUsersRepo repo;

    public UserAddedEventListener(ChatUsersRepo repo) {
        this.repo = repo;
    }


    @EventListener
    public ChatUser userAdded(UserAddedEvent event) {
        return repo.save(new ChatUser(event.getUserId(), event.getUserName(), false));
    }

}
