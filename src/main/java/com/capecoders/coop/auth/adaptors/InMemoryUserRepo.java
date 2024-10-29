package com.capecoders.coop.auth.adaptors;

import com.capecoders.coop.auth.core.CoopUser;
import com.capecoders.coop.auth.core.UserRepo;

import java.util.ArrayList;
import java.util.List;

public class InMemoryUserRepo implements UserRepo {
    private final List<CoopUser> data = new ArrayList<>(){};

    @Override
    public List<CoopUser> getAllUsers() {
        return data;
    }

    @Override
    public CoopUser saveUser(CoopUser coopUser) {
        data.add(coopUser);
        return coopUser;
    }

    @Override
    public CoopUser getUserByEmail(String email) {
        return data.stream().filter(user -> user.getEmail().equals(email)).findFirst().orElse(null);
    }
}
