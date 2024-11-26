package com.capecoders.coop.auth.core.sendinvite;

import org.springframework.stereotype.Component;

@Component
public class TestSendUserInviteEmail implements SendUserInviteEmail{
    private Boolean returnValue;
    private SendUserInviteRequest request;

    public TestSendUserInviteEmail() {
        this.makeSuccess();
    }

    @Override
    public Boolean sendEmail(SendUserInviteRequest request) {
        this.request = request;
        return returnValue;
    }

    public void makeFail() {
        this.returnValue = false;
    }

    public void makeSuccess() {
        this.returnValue = true;
    }


    public  SendUserInviteRequest emailSent() {
        return this.request;
    }


}
