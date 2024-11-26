package com.capecoders.coop.chat;

import com.capecoders.coop.auth.core.LoginService;
import com.capecoders.coop.chat.sendmessage.SendMessageInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.lang.NonNull;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WebSocketIntegrationTest {
    @LocalServerPort
    private int port;

    @Autowired
    private SendMessageInterface messageService; // Inject your service class here
    @Autowired
    private LoginService loginService;

    private StompSession stompSession;


    @BeforeEach
    void setup() throws Exception {
        StandardWebSocketClient standardWebSocketClient = new StandardWebSocketClient();
        SockJsClient sockJsClient = new SockJsClient(Collections.singletonList(new WebSocketTransport(standardWebSocketClient)));
        WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
        stompClient.setMessageConverter(new StringMessageConverter());
        String webSocketUrl = "ws://localhost:" + port + "/websocket-endpoint"; // Make sure this points to your WebSocket endpoint

        // Set up Basic Authentication headers
        WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        String login = loginService.login("wow@wow.com", "test123!");
        headers.add("Authorization", "Bearer " + login);

        StompSessionHandler sessionHandler = new TestSessionHandler();
        stompSession = stompClient.connectAsync(webSocketUrl, headers, sessionHandler).get(10, TimeUnit.SECONDS);
    }

    @Test
    @WithMockUser(username = "wow@wow.com", roles = {"USER"})
    void shouldReceiveMessageFromServer() throws InterruptedException {
        String destination = "/user/chat/messages";
        String messageToSend = "Test message";
        CountDownLatch messageLatch = new CountDownLatch(1);

        stompSession.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders headers) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders headers, Object payload) {
                assertEquals(messageToSend, payload);
                messageLatch.countDown(); // Signal that the message was received
            }
        });
        Thread.sleep(250);

        messageService.sendMessage(messageToSend, "wow@wow.com");

        assertTrue(messageLatch.await(10, TimeUnit.SECONDS), "Message was not received in time");
    }

    private static class TestSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(@NonNull StompSession session, @NonNull StompHeaders connectedHeaders) {
            // Handle post connection activities
        }

        @Override
        public void handleException(@NonNull StompSession session, StompCommand command, @NonNull StompHeaders headers, @NonNull byte[] payload, @NonNull Throwable exception) {
            throw new RuntimeException(exception);
        }
    }




}
