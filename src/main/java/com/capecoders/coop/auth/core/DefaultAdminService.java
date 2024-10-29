package com.capecoders.coop.auth.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class DefaultAdminService {

    public DefaultAdminService(
        UserRepo userRepo,
        PasswordEncoder passwordEncoder,
        @Value("${coop-admin.email}")
        String email,
        @Value("${coop-admin.password}")
        String password) {

        if (userRepo.getUserByEmail(email) == null) {
            userRepo.saveUser(new CoopUser(null, email, passwordEncoder.encode(password)));
        }
    }
}
