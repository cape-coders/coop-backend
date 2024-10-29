package com.capecoders.coop.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Jwts;

public class TestJwtValidator {
    public TestJwtValidator() {
    }

    public Jwt<Header, Claims> isParsableJwt(String token) {
        try {
            return Jwts.parserBuilder()
                .build()
                .parseClaimsJwt(token);

        } catch (Exception e) {
            return null;
        }
    }
}
