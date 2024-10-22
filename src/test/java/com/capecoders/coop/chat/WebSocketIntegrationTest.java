package com.capecoders.coop.chat;

import com.capecoders.coop.chat.sendmessage.SendMessageInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.Base64;
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
        String auth = "testuser:password";
        byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes());
        String authHeader = "Basic " + new String(encodedAuth);
        headers.add("Authorization", authHeader);

        StompSessionHandler sessionHandler = new TestSessionHandler();
        stompSession = stompClient.connect(webSocketUrl, headers, sessionHandler).get(1, TimeUnit.SECONDS);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"USER"})
    void shouldReceiveMessageFromServer() throws InterruptedException {
        String destination = "/user/chat/messages";
        String messageToSend = "Test message";
        CountDownLatch latch = new CountDownLatch(1);

        stompSession.subscribe(destination, new StompFrameHandler() {
            @Override
            public Type getPayloadType(StompHeaders stompHeaders) {
                return String.class;
            }

            @Override
            public void handleFrame(StompHeaders stompHeaders, Object payload) {
                assertEquals(messageToSend, payload);
                latch.countDown();
            }
        });

        // Call the method from your service class instead of sending directly through the WebSocket
        messageService.sendMessage(messageToSend, "testuser");

        assertTrue(latch.await(20, TimeUnit.SECONDS));
    }

    private static class TestSessionHandler extends StompSessionHandlerAdapter {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            // Handle post connection activities
        }

        @Override
        public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
            throw new RuntimeException(exception);
        }
    }




}
