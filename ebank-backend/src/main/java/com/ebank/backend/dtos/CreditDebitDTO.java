package com.ebank.backend.dtos;

import lombok.Data;

@Data
public class CreditDebitDTO {
    private String accountId;
    private double amount;
    private String description;
}
