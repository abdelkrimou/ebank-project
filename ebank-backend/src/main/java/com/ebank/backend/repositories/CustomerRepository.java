package com.ebank.backend.repositories;

import com.ebank.backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    List<Customer> findByNameContainingIgnoreCase(String keyword);

    @Query("select c from Customer c where c.name like :kw")
    List<Customer> searchCustomer(String kw);
}
