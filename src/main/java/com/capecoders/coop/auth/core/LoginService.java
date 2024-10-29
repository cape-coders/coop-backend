package com.capecoders.coop.auth.core;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class LoginService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    public LoginService(UserRepo userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String email, String password) {
        CoopUser coopUserByEmail = userRepo.getUserByEmail(email);
        if (coopUserByEmail == null || !passwordEncoder.matches(password, coopUserByEmail.getPassword())) {
            return null;
        }
        return createJwt(email);
    }

    private String createJwt(String email) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256); // Generates a secure random key for HMAC-SHA256

        return Jwts.builder()
            .setSubject(email)        // Set a subject
//            .claim("role", "admin")    // Add custom claims
//            .signWith(key)             // Sign with the generated key
            .compact();                // Build the JWT
    }
}
