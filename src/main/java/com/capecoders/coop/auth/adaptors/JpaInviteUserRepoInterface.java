package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.core.sendinvite.UserInvite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaInviteUserRepoInterface extends JpaRepository<UserInvite, Long> {
    UserInvite findByEmail(String email);
}
