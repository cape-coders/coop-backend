package com.capecoders.coop.chat;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;


    @PostMapping("chat")
    public NewChatResponse newChat(@RequestBody NewChatRequest request) {
        return chatService.newChat(request);
    }
}
