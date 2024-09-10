package com.capecoders.coop.chat;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ChatService {
    private final ChatAdaptor adaptor;

    public NewChatResponse newChat(NewChatRequest request) {
        return new NewChatResponse(adaptor.sendMessage(new SendMessageRequest()));
    }

}
