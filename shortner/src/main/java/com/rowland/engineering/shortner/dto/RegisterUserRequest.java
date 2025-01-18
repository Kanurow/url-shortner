package com.rowland.engineering.shortner.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class RegisterUserRequest {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @Size(min = 4, message = "minimum username is 4 letters")
    private String username;
    @Email(message = "Must be a valid email format")
    private String email;
    @NotEmpty
    @Size(min = 1, message = "Must be assigned at lease one role")
    private Set<String> roles;

    @Size(min = 4, message = "Must not be less than 4 letters")
    private String password;
}
