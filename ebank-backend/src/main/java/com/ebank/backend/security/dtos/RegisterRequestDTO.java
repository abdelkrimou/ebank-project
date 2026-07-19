package com.ebank.backend.security.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    @NotEmpty
    private String username;
    @NotEmpty
    private String password;
}
