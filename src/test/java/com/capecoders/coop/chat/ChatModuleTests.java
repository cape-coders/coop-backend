package com.capecoders.coop.chat;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;

import com.capecoders.coop.chat.addnewchat.adaptors.InMemoryChatRepo;
import com.capecoders.coop.chat.getchats.GetChatsForUser;
import com.capecoders.coop.chat.sendmessage.SendMessageRequest;
import com.capecoders.coop.chat.sendmessage.SendMessageToChat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.capecoders.coop.chat.addnewchat.AddNewChat;
import com.capecoders.coop.chat.addnewchat.NewChatRequest;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import com.capecoders.coop.events.UserAddedEvent;

import java.util.List;

public class ChatModuleTests {

    private AddNewChat addNewChat;
    private InMemoryChatRepo chatRepo;
    private UserAddedEventListener userAddedEventListener;
    private InMemoryChatUsersRepo chatUsersRepo;
    private SendMessageToChat sendMessageToChat;
    private TestSendMessage testSendMessage;
    private GetChatsForUser getChatsForUser;

    @BeforeEach
    public void setUp() {
        testSendMessage = new TestSendMessage();
        chatUsersRepo = new InMemoryChatUsersRepo();
        userAddedEventListener = new UserAddedEventListener(chatUsersRepo);
        chatRepo = new InMemoryChatRepo();
        getChatsForUser = new GetChatsForUser(chatRepo);
        sendMessageToChat = new SendMessageToChat(chatUsersRepo,chatRepo, testSendMessage);
        addNewChat = new AddNewChat(
            chatUsersRepo,
            chatRepo
        );
    }

    @Test
    public void aNewChat_shouldThrowANoUserError_whenNoUsersFoundForRequest() {
        assertThrows(RuntimeException.class, () -> {
            addNewChat.execute(testChatRequest());
        });
    }


    @Test
    public void aNewChat_shouldReturnANewChat_whenValidUserFoundForRequest_andNoPreviousChat() {
        addUser();
        NewChatResponse response = addNewChat.execute(testChatRequest());

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


    @Test
    public void sendingAMessage_shouldUseWebsockets_whenUserIsConnected() {
        ChatUser chatUser = addUser();
        NewChatResponse newChatResponse = addNewChat.execute(testChatRequest());
        assertEquals(0, newChatResponse.getMessages().size());

        userIsConnectedToWebsocket(chatUser);

        sendMessageToChat.execute(new SendMessageRequest(chatUser.getUserId(), "a great test message", newChatResponse.getChatId()));

        assertTrue(testSendMessage.hasMessage("a great test message"));
    }

    @Test
    public void sendingAMessage_shouldNotUseWebsockets_whenUserIsNotConnected() {
        ChatUser chatUser = addUser();
        NewChatResponse newChatResponse = addNewChat.execute(testChatRequest());
        assertEquals(0, newChatResponse.getMessages().size());

        sendMessageToChat.execute(new SendMessageRequest(chatUser.getUserId(), "a great test message", newChatResponse.getChatId()));

        assertFalse(testSendMessage.hasMessage("a great test message"));
    }

    @Test
    public void sendingAMessage_shouldNotSendMessagesFromUsersNotInTheChat() {
        ChatUser chatUser = addUser();
        NewChatResponse newChatResponse = addNewChat.execute(testChatRequest());
        assertEquals(0, newChatResponse.getMessages().size());

        assertThrows(Exception.class, () -> sendMessageToChat.execute(new SendMessageRequest(5L, "a great test message", newChatResponse.getChatId())));
    }

    @Test
    public void getChatsForUser_shouldReturnUserChats() {
        ChatUser chatUser = addUser();
        addNewChat.execute(testChatRequest());

        List<NewChatResponse> response = getChatsForUser.execute(chatUser.getUserId());
        assertEquals(1, response.size());
    }


    private void userIsConnectedToWebsocket(ChatUser chatUser) {
        chatUser.userHasConnectedToWS();
        chatUsersRepo.save(chatUser);
    }


    private ChatUser addUser() {
        return userAddedEventListener.userAdded(new UserAddedEvent(1L, "Chatty Kathy"));
    }

    private static NewChatRequest testChatRequest() {
        return new NewChatRequest(1L, singletonList(1L));
    }
}
