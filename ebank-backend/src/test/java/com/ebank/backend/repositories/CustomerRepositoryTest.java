package com.ebank.backend.repositories;

import com.ebank.backend.entities.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void shouldSaveAndFindCustomer() {
        Customer customer = new Customer();
        customer.setName("Amine Test");
        customer.setEmail("amine.test@ebank.com");

        Customer saved = customerRepository.save(customer);

        assertThat(saved.getId()).isNotNull();
        Customer found = customerRepository.findById(saved.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("Amine Test");
    }

    @Test
    void shouldSearchCustomerByKeyword() {
        Customer customer = new Customer();
        customer.setName("Zineb Alami");
        customer.setEmail("zineb@ebank.com");
        customerRepository.save(customer);

        List<Customer> results = customerRepository.searchCustomer("%Zineb%");

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Zineb Alami");
    }

    @Test
    void shouldReturnEmptyListWhenNoMatch() {
        List<Customer> results = customerRepository.searchCustomer("%NoSuchName%");
        assertThat(results).isEmpty();
    }
}
