package com.ebank.backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI ebankOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("E-Bank - Bank Account Management API")
                .description("REST API for managing customers, bank accounts (current/saving) and operations (debit/credit/transfer). " +
                        "Part 1 of the project - JPA + Services + REST + Swagger.")
                .version("v1.0")
                .contact(new Contact().name("E-Bank Team")));
    }
}
