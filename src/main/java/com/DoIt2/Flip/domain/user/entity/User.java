package com.DoIt2.Flip.domain.user.entity;

import com.DoIt2.Flip.domain.user.enums.Role;
import com.DoIt2.Flip.domain.user.enums.Theme;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Entity(name = "users")
@Getter
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userId;

    private String email; // email

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    @Enumerated(EnumType.STRING)
    private Theme theme;
}
