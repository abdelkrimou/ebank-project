package com.ebank.backend.security.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ChangePasswordDTO {
    @NotEmpty
    private String currentPassword;
    @NotEmpty
    private String newPassword;
}
