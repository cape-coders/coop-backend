package com.capecoders.coop.chat.sendmessage;

import com.capecoders.coop.chat.ChatUser;
import com.capecoders.coop.chat.ChatUsersRepo;
import com.capecoders.coop.chat.addnewchat.Chat;
import com.capecoders.coop.chat.addnewchat.ChatRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SendMessageToChat {

    private final ChatUsersRepo chatUsersRepo;
    private final ChatRepo chatRepo;
    private final SendMessageInterface sendMessageInterface;

    public SendMessageToChat(ChatUsersRepo chatUsersRepo, ChatRepo chatRepo, SendMessageInterface sendMessageInterface) {
        this.chatUsersRepo = chatUsersRepo;
        this.chatRepo = chatRepo;
        this.sendMessageInterface = sendMessageInterface;
    }

    @Transactional
    public void execute(SendMessageRequest request) {
        Chat chat = chatRepo.getChatById(request.getChatId());
        chat.sendMessage(request);
        for (Long userId : chat.getUsers()) {
            Optional<ChatUser> userByUserId = chatUsersRepo.findUserByUserId(userId);
            userByUserId.ifPresent(user -> {
                if (user.getConnectedToWebsockets()) {
                    sendMessageInterface.sendMessage(request.getMessage(), userId.toString());
                }
            });

        }
    }
}
