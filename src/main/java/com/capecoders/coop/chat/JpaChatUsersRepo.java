package com.capecoders.coop.chat;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaChatUsersRepo implements ChatUsersRepo {
    private final JpaChatUsersRepository repo;

    public JpaChatUsersRepo(JpaChatUsersRepository repo) {
        this.repo = repo;
    }

    @Override
    public Optional<ChatUser> findUserByUserId(Long userId) {
        return repo.findById(userId);
    }

    @Override
    public ChatUser save(ChatUser user) {
        return repo.save(user);
    }
}
