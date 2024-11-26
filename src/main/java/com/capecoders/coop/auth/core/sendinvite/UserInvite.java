package com.capecoders.coop.auth.core.sendinvite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.User;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInvite {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String code;
    private OffsetDateTime emailSentAt;

    public static UserInvite create(String email) {
        return new UserInvite(null, email, createInviteCode(), null);
    }

    public void emailSentSuccessfully() {
        this.emailSentAt = OffsetDateTime.now();
    }

    private static String createInviteCode() {
        return UUID.randomUUID().toString();
    }
}
