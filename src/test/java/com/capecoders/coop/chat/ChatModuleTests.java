package com.capecoders.coop.chat;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.capecoders.coop.chat.addnewchat.InMemoryChatRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capecoders.coop.chat.addnewchat.AddNewChat;
import com.capecoders.coop.chat.addnewchat.NewChatRequest;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import com.capecoders.coop.events.UserAddedEvent;

public class ChatModuleTests {

    private AddNewChat addNewChat;
    private InMemoryChatRepo chatRepo;
    private InMemoryChatUsersRepo chatUsersRepo;
    private UserAddedEventListener userAddedEventListener;

    @BeforeEach
    public void setUp() {
        chatUsersRepo = new InMemoryChatUsersRepo();
        userAddedEventListener = new UserAddedEventListener(chatUsersRepo);
        chatRepo = new InMemoryChatRepo();
        addNewChat = new AddNewChat(
            chatUsersRepo,
            chatRepo
        );
    }

    @Test
    public void aNewChat_shouldThrowANoUserError_whenNoUsersFoundForRequest() {
        assertThrows(RuntimeException.class, () -> {
            addNewChat.execute( testChatRequest());
        });
    }


    @Test
    public void aNewChat_shouldReturnANewChat_whenValidUserFoundForRequest_andNoPreviousChat() {
        addUser();
        NewChatResponse response = addNewChat.execute( testChatRequest());

        assertNotNull(response.getChatId());
        assertEquals(singletonList(1L), response.getUsersInChat());
        assertEquals(emptyList(), response.getMessages());
    }


    @Test
    public void aNewChat_shouldReturnPreviousChat_whenValidUserFoundForRequest_andThereAlreadyIsAPreviousChat() {
        addUser();
        NewChatRequest request = testChatRequest();
        NewChatResponse firstChat = addNewChat.execute(request);
        NewChatResponse secondChat = addNewChat.execute(request);

        assertNotNull(chatRepo.getChatById(firstChat.getChatId()));
        assertNotNull(chatRepo.getChatById(secondChat.getChatId()));
        assertEquals(firstChat.getChatId(), secondChat.getChatId());
    }

    private void addUser() {
        userAddedEventListener.userAdded(new UserAddedEvent(1L, "Chatty Kathy"));
    }

    private static NewChatRequest testChatRequest() {
        return new NewChatRequest(1L, singletonList(1L));
    }
}
