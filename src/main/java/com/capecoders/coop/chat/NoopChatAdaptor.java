package com.capecoders.coop.chat;

import org.springframework.stereotype.Component;

@Component
public class NoopChatAdaptor implements ChatAdaptor {

    @Override
    public Boolean sendMessage(SendMessageRequest request) {
        return true;
    }

}
