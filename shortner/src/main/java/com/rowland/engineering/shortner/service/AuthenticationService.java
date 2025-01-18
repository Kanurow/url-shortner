package com.rowland.engineering.shortner.service;

import com.rowland.engineering.shortner.dto.LoginUserRequest;
import com.rowland.engineering.shortner.dto.RegisterUserRequest;
import com.rowland.engineering.shortner.dto.RegisterUserResponse;
import org.springframework.http.ResponseEntity;

public interface AuthenticationService {
    ResponseEntity<?> registerNewUser(RegisterUserRequest registerUser);

    ResponseEntity<?> loginUser(LoginUserRequest loginRequest);
}
