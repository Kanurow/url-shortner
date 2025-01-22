package com.rowland.engineering.shortner.utils;


import com.rowland.engineering.shortner.entity.Role;
import com.rowland.engineering.shortner.entity.RoleName;
import com.rowland.engineering.shortner.entity.UrlMapping;
import com.rowland.engineering.shortner.entity.User;
import com.rowland.engineering.shortner.repository.RoleRepository;
import com.rowland.engineering.shortner.repository.UrlMappingRepository;
import com.rowland.engineering.shortner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UrlMappingRepository urlMappingRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        List<Role> roles = Arrays.asList(
                new Role(RoleName.ROLE_USER),
                new Role(RoleName.ROLE_ADMIN)
        );
        roleRepository.saveAll(roles);

        List<User> users = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            User user = new User();
            user.setUsername("user" + i);
            user.setEmail("user" + i + "@example.com");
            user.setPassword(passwordEncoder.encode("password" + i)); // Encrypt passwords
            users.add(user);
        }
        userRepository.saveAll(users);

        // Initialize URL mappings
        List<UrlMapping> urlMappings = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            if (i == 1) {
                UrlMapping urlMapping = new UrlMapping();
                urlMapping.setShortUrl(generateShortUrl());
                urlMapping.setOriginalUrl("https://example.com");
                urlMapping.setUserId(1L);
                urlMappings.add(urlMapping);
            }
            UrlMapping urlMapping = new UrlMapping();
            urlMapping.setShortUrl(generateShortUrl());
            urlMapping.setOriginalUrl("https://example.com" + i);
            urlMapping.setUserId(1L);
            urlMappings.add(urlMapping);
        }
        urlMappingRepository.saveAll(urlMappings);

        System.out.println("Initialized 50 records in the database.");

    }

    private String generateShortUrl() {
        return UUID.randomUUID().toString().substring(0, 8); // Generate a random 8-character short URL
    }

}

