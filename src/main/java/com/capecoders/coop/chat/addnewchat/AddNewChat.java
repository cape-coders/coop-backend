package com.capecoders.coop.chat.addnewchat;

import java.util.ArrayList;
import java.util.Optional;

import com.capecoders.coop.chat.ChatUser;
import com.capecoders.coop.chat.ChatUsersRepo;


public class AddNewChat {
    private final ChatUsersRepo repo;

    public AddNewChat(ChatUsersRepo repo) {
        this.repo = repo;
    }

    public NewChatResponse execute(NewChatRequest request) {
        Optional<ChatUser> user = repo.findUserByUserId(request.getChatOwnerId());
        if(user.isEmpty()) {
            throw new RuntimeException("No users exist pal");
        } else {
            return new NewChatResponse(1L, request.getChatMembers(), new ArrayList<>(){});
        }

    }
        

}
