package com.capecoders.coop.chat.addnewchat;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewChatRequest {

    private final Long chatOwnerId;
    private final List<Long> chatMembers;
}


