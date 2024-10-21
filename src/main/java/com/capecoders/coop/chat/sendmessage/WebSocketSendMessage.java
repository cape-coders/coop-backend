package com.capecoders.coop.chat.sendmessage;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketSendMessage implements SendMessageInterface{
    private final SimpMessagingTemplate messagingTemplate;


    public WebSocketSendMessage(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void sendMessage(String message, Long userToSendTo) {
        messagingTemplate.convertAndSend("/user/"+userToSendTo, message);
    }


}
