package com.capecoders.coop.auth.core.sendinvite;

import org.springframework.stereotype.Service;

@Service
public class InviteUserService {
    private final SendUserInviteEmail sendUserInviteEmail;
    private final UserInviteRepo userInviteRepo;

    public InviteUserService(SendUserInviteEmail sendUserInviteEmail, UserInviteRepo userInviteRepo) {
        this.sendUserInviteEmail = sendUserInviteEmail;
        this.userInviteRepo = userInviteRepo;
    }

    public Boolean invite(String email) {
        Boolean didEmailSend = sendUserInviteEmail.sendEmail();
        if (didEmailSend) {
            userInviteRepo.save(new UserInvite(null, email, "1234"));
        }
        return didEmailSend;
    }
}
