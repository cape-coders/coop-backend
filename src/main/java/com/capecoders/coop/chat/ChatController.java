package com.capecoders.coop.chat;

import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@AllArgsConstructor
public class ChatController {
    private final ChatService chatService;


    @PostMapping("chat")
    public NewChatResponse newChat(@RequestBody NewChatRequest request) {
        return chatService.newChat(request);
    }
}
