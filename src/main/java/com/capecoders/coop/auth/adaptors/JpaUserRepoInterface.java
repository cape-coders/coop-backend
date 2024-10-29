package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.core.CoopUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepoInterface extends JpaRepository<CoopUser, Long> {
    CoopUser getUserByEmail(String email);
}
