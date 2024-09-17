package com.capecoders.coop.chat;

import java.util.Optional;

public interface ChatUsersRepo {
    Optional<ChatUser> findUserByUserId(Long userId);
    ChatUser save(ChatUser user);
}
