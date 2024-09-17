package com.capecoders.coop.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InMemoryChatUsersRepo implements ChatUsersRepo {
    private List<ChatUser> data = new ArrayList<>(){};

    @Override
    public Optional<ChatUser> findUserByUserId(Long userId) {
        return data.stream().findFirst();
    }

    @Override
    public ChatUser save(ChatUser user) {
        data.add(user);
        return user;
    }

}
