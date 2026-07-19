package com.ebank.backend.dtos;

/**
 * Marker interface implemented by CurrentBankAccountDTO and SavingBankAccountDTO
 * so the service layer can return either type polymorphically
 * (e.g. from listBankAccounts()).
 */
public interface BankAccountDTO {
}
