package com.ebank.backend.web;

import com.ebank.backend.dtos.*;
import com.ebank.backend.exceptions.BalanceNotSufficientException;
import com.ebank.backend.exceptions.BankAccountNotFoundException;
import com.ebank.backend.exceptions.CustomerNotFoundException;
import com.ebank.backend.services.BankAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Tag(name = "Bank Accounts", description = "Account creation, lookup, debit/credit/transfer operations")
public class BankAccountRestController {

    private final BankAccountService bankAccountService;

    @GetMapping("/accounts")
    public List<BankAccountDTO> listAccounts() {
        return bankAccountService.bankAccountList();
    }

    @GetMapping("/accounts/{accountId}")
    public BankAccountDTO getBankAccount(@PathVariable String accountId) throws BankAccountNotFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @GetMapping("/accounts/{accountId}/operations")
    public List<AccountOperationDTO> accountHistory(@PathVariable String accountId) {
        return bankAccountService.accountHistory(accountId);
    }

    @PostMapping("/accounts/current/{customerId}")
    public CurrentBankAccountDTO createCurrentAccount(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") double initialBalance,
            @RequestParam(defaultValue = "0") double overDraft) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(initialBalance, overDraft, customerId);
    }

    @PostMapping("/accounts/saving/{customerId}")
    public SavingBankAccountDTO createSavingAccount(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") double initialBalance,
            @RequestParam(defaultValue = "0.02") double interestRate) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(initialBalance, interestRate, customerId);
    }

    @PostMapping("/accounts/debit")
    public CreditDebitDTO debit(@RequestBody CreditDebitDTO request)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.debit(request.getAccountId(), request.getAmount(), request.getDescription());
        return request;
    }

    @PostMapping("/accounts/credit")
    public CreditDebitDTO credit(@RequestBody CreditDebitDTO request) throws BankAccountNotFoundException {
        bankAccountService.credit(request.getAccountId(), request.getAmount(), request.getDescription());
        return request;
    }

    @PostMapping("/accounts/transfer")
    public void transfer(@RequestBody TransferRequestDTO request)
            throws BankAccountNotFoundException, BalanceNotSufficientException {
        bankAccountService.transfer(request.getAccountSource(), request.getAccountDestination(), request.getAmount());
    }
}
