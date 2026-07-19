package com.ebank.backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

/**
 * A single DEBIT or CREDIT movement on a BankAccount.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    private String description;

    @ManyToOne
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private BankAccount bankAccount;

    /**
     * Username of the authenticated user who performed this operation.
     * Filled in from SecurityContextHolder once Part 3 (JWT) is added.
     */
    private String performedBy;
}
