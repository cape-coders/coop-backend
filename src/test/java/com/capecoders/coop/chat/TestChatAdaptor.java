package com.capecoders.coop.chat;

public class TestChatAdaptor implements ChatAdaptor{

    private final Boolean fakeResult;
    private SendMessageRequest sentRequest;

    public TestChatAdaptor(Boolean fakeResult){
        this.fakeResult = fakeResult;
    }

    @Override
    public Boolean sendMessage(SendMessageRequest request) {
        this.sentRequest = request;
        return fakeResult;
    }

}
