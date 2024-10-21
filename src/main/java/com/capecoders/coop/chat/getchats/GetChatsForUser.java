package com.capecoders.coop.chat.getchats;

import com.capecoders.coop.chat.addnewchat.ChatRepo;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import com.capecoders.coop.chat.addnewchat.adaptors.InMemoryChatRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class GetChatsForUser {
    private ChatRepo chatRepo;
    public GetChatsForUser(ChatRepo chatRepo) {
        this.chatRepo = chatRepo;
    }

    public List<NewChatResponse> execute(Long userId) {
        return Collections.singletonList(
            NewChatResponse.fromChat(chatRepo.getUserSelfChat(userId))
        );
    }
}
