package com.capecoders.coop.auth.core.sendinvite;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
