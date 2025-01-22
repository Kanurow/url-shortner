package com.rowland.engineering.shortner.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UrlMappingDTO {
    private long urlMappingId;
    private long userId;
    private long clickCount;
    private LocalDateTime createdAt;
    private String shortUrl;
    private String originalUrl;
}
