package com.capecoders.coop.chat;

import com.capecoders.coop.chat.sendmessage.SendMessageInterface;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

public class TestSendMessage implements SendMessageInterface {
    private List<String> messages = new ArrayList<String>();

    @Override
    public void sendMessage(String message, String userToSendTo) {
        this.messages.add(message);
    }

    public boolean hasMessage(String message) {
        return this.messages.contains(message);
    }
}
