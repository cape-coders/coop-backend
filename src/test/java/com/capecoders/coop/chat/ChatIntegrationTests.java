package com.capecoders.coop.chat;

import com.capecoders.coop.SecurityConfig;
import com.capecoders.coop.chat.addnewchat.AddNewChat;
import com.capecoders.coop.chat.addnewchat.NewChatRequest;
import com.capecoders.coop.chat.addnewchat.NewChatResponse;
import com.capecoders.coop.chat.sendmessage.SendMessageToChat;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChatController.class)
@Import(SecurityConfig.class)
public class ChatIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AddNewChat service;
    @MockBean
    private SendMessageToChat sendMessageToChat;

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void postToChatShouldCallAddNewChatService() throws Exception {
        when(service.execute(any())).thenReturn(new NewChatResponse(
            1L, singletonList(1L), new ArrayList<>()
        ));
        // Create a sample request object
        NewChatRequest request = new NewChatRequest(
            1L,
            singletonList(1L)
        );

        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/chat")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void postToChatShouldHandleWhenAddNEwUserThrows() throws Exception {
        when(service.execute(any())).thenThrow(new RuntimeException("No users exist pal"));

        NewChatRequest request = new NewChatRequest(
            1L,
            singletonList(1L)
        );

        String jsonRequest = objectMapper.writeValueAsString(request);
        this.mockMvc.perform(post("/chat")
                .content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().is5xxServerError());
    }
}
