package com.ebank.backend.services;

import com.ebank.backend.entities.*;
import com.ebank.backend.exceptions.BalanceNotSufficientException;
import com.ebank.backend.mappers.BankAccountMapperImpl;
import com.ebank.backend.repositories.AccountOperationRepository;
import com.ebank.backend.repositories.BankAccountRepository;
import com.ebank.backend.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BankAccountServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private AccountOperationRepository accountOperationRepository;

    private BankAccountServiceImpl bankAccountService;

    @BeforeEach
    void setUp() {
        bankAccountService = new BankAccountServiceImpl(
                customerRepository, bankAccountRepository, accountOperationRepository, new BankAccountMapperImpl());
    }

    @Test
    void debitShouldThrowWhenBalanceInsufficient() {
        CurrentAccount account = new CurrentAccount();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(100);
        account.setOverDraft(0);
        account.setCreatedAt(new Date());

        when(bankAccountRepository.findById(account.getId())).thenReturn(Optional.of(account));

        assertThatThrownBy(() -> bankAccountService.debit(account.getId(), 500, "test debit"))
                .isInstanceOf(BalanceNotSufficientException.class);
    }

    @Test
    void debitShouldSucceedWithinOverdraft() throws Exception {
        CurrentAccount account = new CurrentAccount();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(100);
        account.setOverDraft(500);
        account.setCreatedAt(new Date());

        when(bankAccountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        bankAccountService.debit(account.getId(), 400, "test debit within overdraft");

        assertThat(account.getBalance()).isEqualTo(-300);
    }

    @Test
    void creditShouldIncreaseBalance() throws Exception {
        SavingAccount account = new SavingAccount();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(1000);
        account.setCreatedAt(new Date());

        when(bankAccountRepository.findById(account.getId())).thenReturn(Optional.of(account));
        when(bankAccountRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        bankAccountService.credit(account.getId(), 250, "deposit");

        assertThat(account.getBalance()).isEqualTo(1250);
    }
}
