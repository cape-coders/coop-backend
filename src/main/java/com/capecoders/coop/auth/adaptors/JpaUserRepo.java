package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaUserRepo implements UserRepo {
    private final JpaUserRepoInterface jpaUserRepoInterface;

    public JpaUserRepo(JpaUserRepoInterface jpaUserRepoInterface) {
        this.jpaUserRepoInterface = jpaUserRepoInterface;
    }

    @Override
    public List<CoopUser> getAllUsers() {
        return jpaUserRepoInterface.findAll();
    }

    @Override
    public CoopUser saveUser(CoopUser coopUser) {
        return jpaUserRepoInterface.save(coopUser);
    }

    @Override
    public CoopUser getUserByEmail(String email) {
        return jpaUserRepoInterface.getUserByEmail(email);
    }
}
