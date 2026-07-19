package com.ebank.backend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Date;
import java.util.List;

/**
 * Base type for all bank accounts. CurrentAccount and SavingAccount both
 * extend this class. Uses SINGLE_TABLE inheritance with a discriminator
 * column ("TYPE") so all accounts live in one table - simplest strategy
 * for this kind of small/medium domain model.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4)
public abstract class BankAccount {

    @Id
    private String id; // UUID string, e.g. account number / IBAN-like identifier

    private double balance;

    private Date createdAt;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Customer customer;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<AccountOperation> accountOperations;

    /**
     * Username of the authenticated user who created this account.
     * Filled in from SecurityContextHolder once Part 3 (JWT) is added.
     */
    private String createdBy;
}
