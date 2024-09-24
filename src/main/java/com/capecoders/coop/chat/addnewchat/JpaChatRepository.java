package com.capecoders.coop.chat.addnewchat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository  extends JpaRepository<Chat, Long>  {
    Chat findChatByUsersContaining(Long userId);
}
