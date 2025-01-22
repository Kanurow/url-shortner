package com.rowland.engineering.shortner.controller;

import com.rowland.engineering.shortner.dto.ClickEventsDTO;
import com.rowland.engineering.shortner.dto.UrlMappingDTO;
import com.rowland.engineering.shortner.projections.UrlMappingProjection;
import com.rowland.engineering.shortner.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/url-shortner")
@RequiredArgsConstructor
public class UrlMappingController {
    private final UrlMappingService urlMappingService;

    @PostMapping("/create")
    public ResponseEntity<UrlMappingDTO> createShortUrl(@RequestBody Map<String, String> request) {

        return urlMappingService.createShortUrl(request);
    }

    @GetMapping("/my-urls")
    public ResponseEntity<List<UrlMappingDTO>> myUrls() {
        return ResponseEntity.ok(urlMappingService.getMyUrls());
    }
    @GetMapping("/my-urls-list")
    public ResponseEntity<Page<UrlMappingProjection>> myUrlsList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "25") int size) {
        Page<UrlMappingProjection> pagedUrls = urlMappingService.getMyPagedUrls(page, size);
        return ResponseEntity.ok(pagedUrls);
    }

    @GetMapping("/analytics/{shortUrl}")
    public ResponseEntity<List<ClickEventsDTO>> getUrlAnalytics(
            @PathVariable String shortUrl,
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {
        List<ClickEventsDTO> analytics = urlMappingService.getUrlAnalytics(shortUrl, startDate, endDate);
        return ResponseEntity.ok(analytics);
    }

    @GetMapping("/analytics/total-clicks")
    public ResponseEntity<Map<LocalDate, Long>> getUrlTotalClicksByDate(@RequestParam("startDate") String startDate,
                                                                        @RequestParam("endDate") String endDate) {
        Map<LocalDate, Long> totalClicksByUserAndDate = urlMappingService.getTotalClicksByUserAndDate(startDate, endDate);
        return ResponseEntity.ok(totalClicksByUserAndDate);

    }
}
