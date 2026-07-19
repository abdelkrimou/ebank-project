package com.ebank.backend.repositories;

import com.ebank.backend.entities.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BankAccountRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @Autowired
    private AccountOperationRepository accountOperationRepository;

    private Customer persistCustomer() {
        Customer customer = new Customer();
        customer.setName("Karim Test");
        customer.setEmail("karim.test@ebank.com");
        return customerRepository.save(customer);
    }

    @Test
    void shouldSaveCurrentAccountAndFetchByCustomer() {
        Customer customer = persistCustomer();

        CurrentAccount account = new CurrentAccount();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(5000);
        account.setOverDraft(1000);
        account.setCreatedAt(new Date());
        account.setStatus(AccountStatus.CREATED);
        account.setCustomer(customer);
        bankAccountRepository.save(account);

        List<BankAccount> accounts = bankAccountRepository.findByCustomerId(customer.getId());

        assertThat(accounts).hasSize(1);
        assertThat(accounts.get(0)).isInstanceOf(CurrentAccount.class);
        assertThat(((CurrentAccount) accounts.get(0)).getOverDraft()).isEqualTo(1000);
    }

    @Test
    void shouldSaveSavingAccountAndAccountOperations() {
        Customer customer = persistCustomer();

        SavingAccount account = new SavingAccount();
        account.setId(UUID.randomUUID().toString());
        account.setBalance(10000);
        account.setInterestRate(3.5);
        account.setCreatedAt(new Date());
        account.setStatus(AccountStatus.ACTIVATED);
        account.setCustomer(customer);
        bankAccountRepository.save(account);

        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(2000);
        operation.setDescription("Initial deposit");
        operation.setOperationDate(new Date());
        operation.setBankAccount(account);
        accountOperationRepository.save(operation);

        List<AccountOperation> operations = accountOperationRepository.findByBankAccountId(account.getId());

        assertThat(operations).hasSize(1);
        assertThat(operations.get(0).getType()).isEqualTo(OperationType.CREDIT);
        assertThat(operations.get(0).getAmount()).isEqualTo(2000);
    }
}
