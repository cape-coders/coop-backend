package com.capecoders.coop.chat;

import com.capecoders.coop.chat.addnewchat.AddNewChat;
import com.capecoders.coop.chat.addnewchat.NewChatRequest;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class ChatController {
    private final AddNewChat addNewChat;

    public ChatController(AddNewChat addNewChat) {
        this.addNewChat = addNewChat;
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
}
