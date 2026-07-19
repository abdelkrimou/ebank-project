package com.ebank.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * A bank customer. One customer can own many bank accounts.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Customer name should not be empty")
    private String name;

    @Email(message = "Email should be valid")
    private String email;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<BankAccount> bankAccounts;

    /**
     * Username of the authenticated user (Spring Security principal) that created
     * this customer record. Populated once Part 3 (JWT security) is wired in via
     * SecurityContextHolder.getContext().getAuthentication().getName().
     */
    private String createdBy;
}
