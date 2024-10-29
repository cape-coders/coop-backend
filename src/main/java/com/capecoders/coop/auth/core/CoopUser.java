package com.capecoders.coop.auth.core;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CoopUser {
    @Id
    @GeneratedValue
    private Long id;

    private String email;
    private String password;
}
