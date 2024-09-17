package com.capecoders.coop.chat;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ChatService {
    private final ChatAdaptor adaptor;

    public NewChatResponse newChat(NewChatRequest request) {
        return new NewChatResponse(adaptor.sendMessage(new SendMessageRequest()));
    }

}
