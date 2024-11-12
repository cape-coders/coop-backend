package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.core.sendinvite.UserInvite;
import com.capecoders.coop.auth.core.sendinvite.UserInviteRepo;
import org.springframework.stereotype.Component;

@Component
public class JpaInviteUserRepo implements UserInviteRepo {
    private final JpaInviteUserRepoInterface inviteRepo;

    public JpaInviteUserRepo(JpaInviteUserRepoInterface inviteRepo) {
        this.inviteRepo = inviteRepo;
    }

    @Override
    public UserInvite getByEmail(String email) {
        return inviteRepo.findByEmail(email);
    }

    @Override
    public UserInvite save(UserInvite userInvite) {
        return inviteRepo.save(userInvite);
    }
}
