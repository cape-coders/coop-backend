package com.capecoders.coop.chat.addnewchat;

public interface ChatRepo {
    Chat addChat(Chat chat);
    Chat getChatById(Long id);
    Chat getUserSelfChat(Long userId);
}
