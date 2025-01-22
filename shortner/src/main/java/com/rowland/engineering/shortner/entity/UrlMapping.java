package com.rowland.engineering.shortner.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;



@Entity
@Data
@Table(name = "url_mapping", uniqueConstraints = {@UniqueConstraint(columnNames = "shortUrl")})
public class UrlMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String shortUrl;
    @NotNull
    private String originalUrl;
    private long clickCount = 0;

    private LocalDateTime createdDate = LocalDateTime.now();


    private long userId;
    private boolean isDeleted = false;
}
