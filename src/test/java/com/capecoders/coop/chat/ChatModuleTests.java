package com.capecoders.coop.chat;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ChatModuleTests {

    @Test
    public void newChat_shouldReturnFailureMessage_whenCannotSend() {
        TestChatAdaptor adaptor = new TestChatAdaptor(false);
        ChatService service = new ChatService(adaptor);

        NewChatRequest request = new NewChatRequest(
            1L, 2L, "this is a message"
        );

        NewChatResponse response = service.newChat(request);

        assertFalse(response.getSuccessful());
    }

    @Test
    public void newChat_shouldReturnSuccessMessage_whenCanSend() {
        TestChatAdaptor adaptor = new TestChatAdaptor(true);
        ChatService service = new ChatService(adaptor);

        NewChatRequest request = new NewChatRequest(
            1L, 2L, "this is a message"
        );

        NewChatResponse response = service.newChat(request);

        assertTrue(response.getSuccessful());
    }
}
