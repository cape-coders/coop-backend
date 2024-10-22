package com.capecoders.coop.chat.sendmessage;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSendMessage implements SendMessageInterface {
    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketSendMessage(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(String message, String userToSendTo) {

        System.out.println("Sending message to user: " + userToSendTo + " -> " + message);
        messagingTemplate.convertAndSendToUser(userToSendTo, "/chat/messages", message);
    }
}
