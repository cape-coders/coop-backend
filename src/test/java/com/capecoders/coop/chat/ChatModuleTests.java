package com.capecoders.coop.chat;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import com.capecoders.coop.chat.addnewchat.AddNewChat;
import com.capecoders.coop.chat.addnewchat.NewChatRequest;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import com.capecoders.coop.events.UserAddedEvent;

public class ChatModuleTests {

    @Test
    public void aNewChat_shouldThrowANoUserError_whenNoUsersFoundForRequest() {
        assertThrows(RuntimeException.class, () -> {
            InMemoryChatUsersRepo repo = new InMemoryChatUsersRepo();
            AddNewChat thing = new AddNewChat(repo);
            NewChatRequest request = new NewChatRequest(1L, singletonList(1L));
            thing.execute(request);
        });
    }

    @Test
    public void aNewChat_shouldReturnANewChat_whenValidUserFoundForRequest_andNoPreviousChat() {
        InMemoryChatUsersRepo repo = new InMemoryChatUsersRepo();
        new UserAddedEventListener(repo).userAdded(new UserAddedEvent(1L, "Chatty Kathy"));

        AddNewChat thing = new AddNewChat(repo);
        NewChatRequest request = new NewChatRequest(1L, singletonList(1L));
        NewChatResponse response = thing.execute(request);

        assertNotNull(response.getChatId());
        assertEquals(singletonList(1L), response.getUsersInChat());
        assertEquals(emptyList(), response.getMessages());
    }

    @Test
    public void aNewChat_shouldReturnPreviousChat_whenValidUserFoundForRequest_andThereAlreadyIsAPreviousChat() {

    }
}
