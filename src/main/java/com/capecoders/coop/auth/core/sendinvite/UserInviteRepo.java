package com.capecoders.coop.auth.core.sendinvite;

public interface UserInviteRepo {
    UserInvite getByEmail(String email);
    UserInvite save(UserInvite userInvite);
}
