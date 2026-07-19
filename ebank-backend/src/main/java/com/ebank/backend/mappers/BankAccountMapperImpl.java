package com.ebank.backend.mappers;

import com.ebank.backend.dtos.*;
import com.ebank.backend.entities.AccountOperation;
import com.ebank.backend.entities.CurrentAccount;
import com.ebank.backend.entities.Customer;
import com.ebank.backend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Manual (non-MapStruct) mapper between entities and DTOs. Kept explicit
 * and dependency-free on purpose so it's easy to read/debug/extend.
 */
@Service
public class BankAccountMapperImpl {

    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDTO(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount) {
        SavingBankAccountDTO dto = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount, dto);
        dto.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        dto.setType("SavingAccount");
        return dto;
    }

    public SavingAccount fromSavingBankAccountDTO(SavingBankAccountDTO dto) {
        SavingAccount account = new SavingAccount();
        BeanUtils.copyProperties(dto, account);
        account.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return account;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount) {
        CurrentBankAccountDTO dto = new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount, dto);
        dto.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        dto.setType("CurrentAccount");
        return dto;
    }

    public CurrentAccount fromCurrentBankAccountDTO(CurrentBankAccountDTO dto) {
        CurrentAccount account = new CurrentAccount();
        BeanUtils.copyProperties(dto, account);
        account.setCustomer(fromCustomerDTO(dto.getCustomerDTO()));
        return account;
    }

    public AccountOperationDTO fromAccountOperation(AccountOperation operation) {
        AccountOperationDTO dto = new AccountOperationDTO();
        BeanUtils.copyProperties(operation, dto);
        return dto;
    }
}
