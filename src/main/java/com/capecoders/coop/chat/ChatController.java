package com.capecoders.coop.chat;

import com.capecoders.coop.chat.addnewchat.AddNewChat;
import com.capecoders.coop.chat.addnewchat.NewChatRequest;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import com.capecoders.coop.chat.sendmessage.SendMessageInterface;
import com.capecoders.coop.chat.sendmessage.SendMessageRequest;
import com.capecoders.coop.chat.sendmessage.SendMessageToChat;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

@RestController
@CrossOrigin
public class ChatController {
    private final AddNewChat addNewChat;
    private final SendMessageToChat sendMessageToChat;
    private final ChatUsersRepo chatUsersRepo;
    private final SendMessageInterface sendMessageInterface;

    public ChatController(AddNewChat addNewChat, SendMessageToChat sendMessageToChat, ChatUsersRepo chatUsersRepo, SendMessageInterface sendMessageInterface) {
        this.addNewChat = addNewChat;
        this.sendMessageToChat = sendMessageToChat;
        this.chatUsersRepo = chatUsersRepo;
        this.sendMessageInterface = sendMessageInterface;
    }

    @MessageMapping("/user/{userToSendTo}/{chatId}")
    public void receiveMessageFromClient(String message, @DestinationVariable Long userToSendTo, @DestinationVariable Long chatId) {
        sendMessageToChat.execute(new SendMessageRequest(userToSendTo, message, chatId));
    }


    @PostMapping(path = "/chat")
    public ResponseEntity<?> newChat(@RequestBody NewChatRequest newChatRequest) {
        try {
            NewChatResponse response = addNewChat.execute(newChatRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/sendchat/{userId}/{message}")
    public ResponseEntity<?> sendChat(@PathVariable String userId, @PathVariable String message) {
        try {
            sendMessageInterface.sendMessage(message, userId);
            return new ResponseEntity<>("Message sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @EventListener
    public void handleWebSocketConnectedListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("Connected: " + headerAccessor.getSessionId());
    }

}
