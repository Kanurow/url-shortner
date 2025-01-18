package com.rowland.engineering.shortner.utils;


import com.rowland.engineering.shortner.entity.Role;
import com.rowland.engineering.shortner.entity.RoleName;
import com.rowland.engineering.shortner.repository.RoleRepository;
import com.rowland.engineering.shortner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = Arrays.asList(
                new Role(RoleName.ROLE_USER),
                new Role(RoleName.ROLE_ADMIN)
        );
        roleRepository.saveAll(roles);

    }

}

