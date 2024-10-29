package com.capecoders.coop.auth.core;

import java.util.List;

public interface UserRepo {
    List<CoopUser> getAllUsers();
    CoopUser saveUser(CoopUser coopUser);
    CoopUser getUserByEmail(String email);
}
