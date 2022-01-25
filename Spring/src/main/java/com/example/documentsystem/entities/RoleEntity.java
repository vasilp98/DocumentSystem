package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = { @JoinColumn(name = "ROLE_ID", referencedColumnName = "id", nullable = false, updatable = false)},
            inverseJoinColumns = { @JoinColumn(name = "USER_ID", referencedColumnName = "id", nullable = false, updatable = false)})
    private Set<UserEntity> users = new HashSet<>();

}
