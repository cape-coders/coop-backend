package com.capecoders.coop;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration

public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Disable CSRF for development purposes, but ensure you enable it in production
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll() // Permit all requests without authentication
            );

        return http.build();
    }
}
