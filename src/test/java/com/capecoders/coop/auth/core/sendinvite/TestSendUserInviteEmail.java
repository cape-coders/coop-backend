package com.capecoders.coop.auth.core.sendinvite;

public class TestSendUserInviteEmail implements SendUserInviteEmail{
    private final Boolean returnValue;

    public TestSendUserInviteEmail(Boolean returnValue) {
        this.returnValue = returnValue;
    }

    @Override
    public Boolean sendEmail() {
        return returnValue;
    }
}
