package com.capecoders.coop.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewChatRequest {

    private final Long senderId;
    private final Long receiverId;
    private final String message;
}


