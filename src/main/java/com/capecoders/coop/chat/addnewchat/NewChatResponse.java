package com.capecoders.coop.chat.addnewchat;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewChatResponse {

    private final Long chatId;
    private final List<Long> usersInChat;
    private final List<String> messages;
}
