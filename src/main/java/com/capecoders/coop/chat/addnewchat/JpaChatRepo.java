package com.capecoders.coop.chat.addnewchat;

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
        return repo.getReferenceById(id);
    }

    @Override
    public Chat getUserSelfChat(Long userId) {
        return repo.findChatByUsersContaining(userId);
    }
}
