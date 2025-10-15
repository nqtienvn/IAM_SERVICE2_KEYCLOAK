package com.tien.iam_service2_keycloak.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;

    private String lastName;
    private String keycloakUserId;
    @Column(nullable = false)
    private Boolean enabled;
    @Column(nullable = false)
    private Boolean deleted = false;
}
