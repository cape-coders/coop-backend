package com.capecoders.coop.chat;

import com.capecoders.coop.events.UserAddedEvent;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

@Service
public class UserAddedEventListener {
    private final ChatUsersRepo repo;

    public UserAddedEventListener(ChatUsersRepo repo) {
        this.repo = repo;
    }

    @PostConstruct
    public void addFakeData() {
        this.userAdded(new UserAddedEvent(1L, "test user"));
    }

    public void userAdded(UserAddedEvent event) {
        repo.save(new ChatUser(event.getUserId(), event.getUserName()));
    }

}
