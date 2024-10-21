package com.capecoders.coop.chat.addnewchat.adaptors;

import com.capecoders.coop.chat.addnewchat.Chat;
import com.capecoders.coop.chat.addnewchat.ChatRepo;
import org.springframework.stereotype.Repository;

@Repository
public class JpaChatRepo implements ChatRepo {
    private final JpaChatRepository repo;

    public JpaChatRepo(JpaChatRepository repo) {
        this.repo = repo;
    }

    @Override
    public Chat addChat(Chat chat) {
        return repo.save(chat);
    }

    @Override
    public Chat getChatById(Long id) {
        return repo.getById(id);
    }

    @Override
    public Chat getUserSelfChat(Long userId) {
        return repo.findChatByUsersContaining(userId);
    }
}
