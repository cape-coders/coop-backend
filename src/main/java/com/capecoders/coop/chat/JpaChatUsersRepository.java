package com.capecoders.coop.chat;

import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaChatUsersRepository extends JpaRepository<ChatUser, Long> {
}
