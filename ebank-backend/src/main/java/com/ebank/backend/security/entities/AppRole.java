package com.ebank.backend.security.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "app_role")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String roleName; // e.g. "ROLE_USER", "ROLE_ADMIN"
}
