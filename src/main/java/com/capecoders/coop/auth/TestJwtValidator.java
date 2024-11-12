package com.capecoders.coop.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.security.Key;

public class TestJwtValidator {
    public TestJwtValidator() {
    }

    public Jws<Claims> isParsableJwt(String token) {
        try {
            Key key = Keys.hmacShaKeyFor("secretkeyisverysecretIpromise!!!".getBytes(StandardCharsets.UTF_8));
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);

        } catch (Exception e) {
            return null;
        }
    }
}
