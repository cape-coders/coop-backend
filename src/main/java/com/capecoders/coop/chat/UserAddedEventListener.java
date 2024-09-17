package com.capecoders.coop.chat;

import com.capecoders.coop.events.UserAddedEvent;

public class UserAddedEventListener {
    private final ChatUsersRepo repo;

    public UserAddedEventListener(ChatUsersRepo repo) {
        this.repo = repo;
    }

    public void userAdded(UserAddedEvent event) {
        repo.save(new ChatUser(event.getUserId(), event.getUserName()));
    }

}
