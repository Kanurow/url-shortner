package com.rowland.engineering.shortner.service;

import com.rowland.engineering.shortner.dto.ClickEventsDTO;
import com.rowland.engineering.shortner.dto.UrlMappingDTO;
import com.rowland.engineering.shortner.entity.UrlMapping;
import com.rowland.engineering.shortner.projections.UrlMappingProjection;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface UrlMappingService {
    ResponseEntity<UrlMappingDTO> createShortUrl(Map<String, String> request);

    List<UrlMappingDTO> getMyUrls();

    Page<UrlMappingProjection> getMyPagedUrls(int page, int size);

    List<ClickEventsDTO> getUrlAnalytics(String shortUrl, String startDate, String endDate);

    Map<LocalDate, Long> getTotalClicksByUserAndDate(String startDate, String endDate);

    UrlMapping getOriginalUrl(String shortUrl);
}
