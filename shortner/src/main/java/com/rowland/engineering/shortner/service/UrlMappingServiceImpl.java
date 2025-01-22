package com.rowland.engineering.shortner.service;

import com.rowland.engineering.shortner.dto.ClickEventsDTO;
import com.rowland.engineering.shortner.dto.UrlMappingDTO;
import com.rowland.engineering.shortner.entity.ClickEvent;
import com.rowland.engineering.shortner.entity.UrlMapping;
import com.rowland.engineering.shortner.entity.User;
import com.rowland.engineering.shortner.exception.PageLimitExceededException;
import com.rowland.engineering.shortner.exception.ResourceNotFoundException;
import com.rowland.engineering.shortner.projections.UrlMappingProjection;
import com.rowland.engineering.shortner.repository.ClickEventRepository;
import com.rowland.engineering.shortner.repository.UrlMappingRepository;
import com.rowland.engineering.shortner.repository.UserRepository;
import com.rowland.engineering.shortner.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UrlMappingServiceImpl implements UrlMappingService{

    private final UserRepository userRepository;
    private final UrlMappingRepository urlMappingRepository;
    private final ClickEventRepository clickEventRepository;

    @Override
    public ResponseEntity<UrlMappingDTO> createShortUrl(Map<String, String> request) {
        String originalUrl = request.get("originalUrl");
        Long userId = SecurityUtils.extractPrincipal().getId();
        System.out.println(userId +" ID -----");


        String shortUrl;
        do {
            shortUrl = generateShortUrl();
            System.out.println("Generated URL: " + shortUrl + ", Exists in DB: " + urlMappingRepository.existsByShortUrlAndUserId(shortUrl, userId));
        } while (urlMappingRepository.existsByShortUrlAndUserId(shortUrl, userId));


        UrlMapping urlMapping = new UrlMapping();
        urlMapping.setShortUrl(shortUrl);
        urlMapping.setOriginalUrl(originalUrl);
        urlMapping.setUserId(userId);
        urlMapping.setCreatedDate(LocalDateTime.now());

        UrlMapping savedMapping = urlMappingRepository.save(urlMapping);
        System.out.println(savedMapping);
        return new ResponseEntity<>(new UrlMappingDTO(savedMapping.getId(), savedMapping.getUserId(), savedMapping.getClickCount(), savedMapping.getCreatedDate(), savedMapping.getShortUrl(), savedMapping.getOriginalUrl()),
                HttpStatus.CREATED);    }

    @Override
    public List<UrlMappingDTO> getMyUrls() {
        Long id = SecurityUtils.extractPrincipal().getId();
        var user = userRepository.getReferenceById(id);
        System.out.println(user);
        System.out.println(id);
        return urlMappingRepository.findActiveUrlsByUser(user.getId()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }

    @Override
    public Page<UrlMappingProjection> getMyPagedUrls(int page, int size) {
        validatePageSize(size);
        PageRequest pageRequest = PageRequest.of(page, size);
        Long userId = SecurityUtils.extractPrincipal().getId();

        return urlMappingRepository.getPagedUserUrls(userId, pageRequest);

    }

    @Override
    public List<ClickEventsDTO> getUrlAnalytics(String shortUrl, String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);
        long userId = SecurityUtils.extractPrincipal().getId();

        UrlMapping urlMapping = urlMappingRepository.findByShortUrlAndUserId(shortUrl, userId).orElseThrow(() -> new ResourceNotFoundException("Url mapping not found"));

        return clickEventRepository.findByUrlMappingAndClickDateBetween(urlMapping, start, end).stream().collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()))
                .entrySet().stream()
                .map(entry -> {
                    ClickEventsDTO clickEventsDTO = new ClickEventsDTO();
                    clickEventsDTO.setClickDate(entry.getKey());
                    clickEventsDTO.setCount(entry.getValue());
                    return clickEventsDTO;
                }).collect(Collectors.toList());

    }

    @Override
    public Map<LocalDate, Long> getTotalClicksByUserAndDate(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        long userId = SecurityUtils.extractPrincipal().getId();
        List<UrlMapping> urlMapping = urlMappingRepository.findActiveUrlsByUser(userId);
        List<ClickEvent> clickEvents = clickEventRepository.findByUrlMappingInAndClickDateBetween(urlMapping, start.atStartOfDay(), end.plusDays(1).atStartOfDay());

        return clickEvents.stream().collect(Collectors.groupingBy(click -> click.getClickDate().toLocalDate(), Collectors.counting()));
    }

    @Override
    @Transactional
    public UrlMapping getOriginalUrl(String shortUrl) {
        var userId = SecurityUtils.extractPrincipal().getId();
        Optional<UrlMapping> byShortUrlAndUserId = urlMappingRepository.findByShortUrlAndUserId(shortUrl, userId);
        if (byShortUrlAndUserId.isPresent()) {
            UrlMapping urlMapping = byShortUrlAndUserId.get();
            urlMapping.setClickCount(urlMapping.getClickCount() + 1);
            urlMappingRepository.save(urlMapping);

            ClickEvent clickEvent = new ClickEvent();
            clickEvent.setUrlMapping(urlMapping);
            clickEvent.setClickDate(LocalDateTime.now());
            clickEventRepository.save(clickEvent);
            return urlMapping;
        } else {
            throw new ResourceNotFoundException("Short url does not exist");
        }
    }


    private String generateShortUrl() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom secureRandom = new SecureRandom();
        StringBuilder shortUrl = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            shortUrl.append(CHARACTERS.charAt(secureRandom.nextInt(CHARACTERS.length())));
        }
        return shortUrl.toString();
    }

    private UrlMappingDTO convertToDTO(UrlMapping urlMapping) {
        UrlMappingDTO urlMappingDTO = new UrlMappingDTO();
        urlMappingDTO.setUrlMappingId(urlMapping.getId());
        urlMappingDTO.setUserId(urlMapping.getUserId());
        urlMappingDTO.setClickCount(urlMapping.getClickCount());
        urlMappingDTO.setOriginalUrl(urlMapping.getOriginalUrl());
        urlMappingDTO.setCreatedAt(urlMapping.getCreatedDate());
        urlMappingDTO.setShortUrl(urlMapping.getShortUrl());
        return urlMappingDTO;
    }
    private static void validatePageSize(int size) {
        if (size > 25) {
            throw new PageLimitExceededException("Page size should not exceed 25 items");
        }
    }
}
