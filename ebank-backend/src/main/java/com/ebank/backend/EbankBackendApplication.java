package com.ebank.backend;

import com.ebank.backend.entities.Customer;
import com.ebank.backend.repositories.CustomerRepository;
import com.ebank.backend.security.entities.AppRole;
import com.ebank.backend.security.entities.AppUser;
import com.ebank.backend.security.repositories.RoleRepository;
import com.ebank.backend.security.repositories.UserRepository;
import com.ebank.backend.services.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootApplication
public class EbankBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(EbankBackendApplication.class, args);
    }

    /**
     * Seeds a couple of customers + accounts + a default admin login on
     * startup, dev-only. Remove/guard this before shipping anywhere real -
     * it creates a well-known "admin/admin123" account.
     */
    @Bean
    CommandLineRunner commandLineRunner(CustomerRepository customerRepository,
                                         BankAccountService bankAccountService,
                                         UserRepository userRepository,
                                         RoleRepository roleRepository,
                                         PasswordEncoder passwordEncoder) {
        return args -> {
            Stream.of("Hassan", "Yassine", "Imane").forEach(name -> {
                Customer customer = new Customer();
                customer.setName(name);
                customer.setEmail(name.toLowerCase() + "@ebank.com");
                customerRepository.save(customer);
            });

            customerRepository.findAll().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random() * 90000, 9000, customer.getId());
                    bankAccountService.saveSavingBankAccount(Math.random() * 120000, 5.5, customer.getId());
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                }
            });

            if (userRepository.findByUsername("admin").isEmpty()) {
                AppRole adminRole = roleRepository.findByRoleName("ROLE_ADMIN")
                        .orElseGet(() -> roleRepository.save(AppRole.builder().roleName("ROLE_ADMIN").build()));

                Set<AppRole> roles = new HashSet<>();
                roles.add(adminRole);

                AppUser admin = AppUser.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123")) // dev-only credential, change immediately
                        .roles(roles)
                        .build();
                userRepository.save(admin);
                System.out.println("Seeded dev admin user -> username: admin / password: admin123 (CHANGE THIS)");
            }
        };
    }
}
