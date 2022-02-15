package com.example.documentsystem.entities;

import com.example.documentsystem.models.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
@NoArgsConstructor
@EqualsAndHashCode(exclude = "permissions")
@ToString(exclude = "permissions")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    @JsonIgnore
    private String password;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.USER;

    private LocalDateTime created = LocalDateTime.now();

    private LocalDateTime modified = LocalDateTime.now();

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_permissions",
            joinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false, updatable = false)},
            inverseJoinColumns = { @JoinColumn(name = "PERMISSION_ID", referencedColumnName = "id", nullable = false, updatable = false)})
    private Set<PermissionEntity> permissions = new HashSet<>();

    public UserEntity(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }
}
