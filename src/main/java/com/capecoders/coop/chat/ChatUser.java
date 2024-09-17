package com.capecoders.coop.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatUser {

    private final Long userId;
    private final String userName;
}
