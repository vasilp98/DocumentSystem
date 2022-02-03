package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String email;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime modified = LocalDateTime.now();

    @ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private Set<RoleEntity> roles = new HashSet<>();

    public UserEntity(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }
}
