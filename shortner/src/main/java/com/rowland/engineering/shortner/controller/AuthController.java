package com.rowland.engineering.shortner.controller;

import com.rowland.engineering.shortner.dto.LoginUserRequest;
import com.rowland.engineering.shortner.dto.RegisterUserRequest;
import com.rowland.engineering.shortner.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @Operation(
            description = "The set of roles available are user and admin roles",
            summary = "Register account information"
    )
    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserRequest registerUser) {
        return authenticationService.registerNewUser(registerUser);
    }

    @Operation(
            description = "Post request for signing in users already existing in our database",
            summary = "Enables user log in - Users can user either username or email address"
    )
    @PostMapping("/sign-in")
    public ResponseEntity<?> loginUser(@Valid @RequestBody LoginUserRequest loginRequest) {
        return authenticationService.loginUser(loginRequest);
    }

}
