package com.capecoders.coop.chat.addnewchat;

import com.capecoders.coop.chat.ChatUser;
import com.capecoders.coop.chat.ChatUsersRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddNewChat {
    private final ChatUsersRepo repo;
    private final ChatRepo chatRepo;

    public AddNewChat(ChatUsersRepo repo, ChatRepo chatRepo) {
        this.repo = repo;
        this.chatRepo = chatRepo;
    }

    public NewChatResponse execute(NewChatRequest request) {
        Optional<ChatUser> user = repo.findUserByUserId(request.getChatOwnerId());
        if (user.isEmpty()) {
            throw new RuntimeException("No users exist pal");
        }
        Chat previousChat = chatRepo.getUserSelfChat(request.getChatOwnerId());
        if (previousChat != null) {
            return NewChatResponse.fromChat(previousChat);
        }

        Chat chat = chatRepo.addChat(Chat.fromAddRequest(request));
        return NewChatResponse.fromChat(chat);
    }


}
