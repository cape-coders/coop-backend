package com.capecoders.coop.chat.addnewchat;

import com.capecoders.coop.chat.sendmessage.SendMessageRequest;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Chat {
    @Id
    @GeneratedValue
    private Long id;
    @ElementCollection
    private List<Long> users;
    @ElementCollection
    private List<String> messages;

    public static Chat fromAddRequest(NewChatRequest request) {
        Chat chat = new Chat();
        chat.setUsers(request.getChatMembers());
        chat.setMessages(new ArrayList<>());
        return chat;
    }

    public void sendMessage(SendMessageRequest request) {
        if (!users.contains(request.getSenderId())) {
            throw new IllegalArgumentException("Sender is not part of the chat");
        }
        this.messages.add(request.getMessage());
    }
}
