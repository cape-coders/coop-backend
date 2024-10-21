package com.capecoders.coop.chat.addnewchat.adaptors;

import com.capecoders.coop.chat.addnewchat.Chat;
import com.capecoders.coop.chat.addnewchat.ChatRepo;

import java.util.ArrayList;
import java.util.List;

public class InMemoryChatRepo implements ChatRepo {
    private final List<Chat> data = new ArrayList<Chat>();
    private Long currentId = 0L;
    @Override
    public Chat addChat(Chat chat) {
        currentId++;
        chat.setId(currentId);
        data.add(chat);
        return chat;
    }

    @Override
    public Chat getChatById(Long id) {
        return data.stream().filter(chat -> chat.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public Chat getUserSelfChat(Long userId) {
        return data.stream().filter(x -> x.getUsers().contains(userId)).findFirst().orElse(null);
    }
}
