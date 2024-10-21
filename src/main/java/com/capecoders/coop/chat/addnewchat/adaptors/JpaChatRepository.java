package com.capecoders.coop.chat.addnewchat.adaptors;

import com.capecoders.coop.chat.addnewchat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaChatRepository  extends JpaRepository<Chat, Long>  {
    Chat findChatByUsersContaining(Long userId);
}
