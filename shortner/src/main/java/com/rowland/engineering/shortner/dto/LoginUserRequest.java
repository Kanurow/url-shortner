package com.rowland.engineering.shortner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginUserRequest {
    @NotBlank
    private String usernameOrEmail;

    @NotBlank
    @Size(min = 4, message = "Password must be at least four characters")
    private String password;
}
