package com.rowland.engineering.shortner.controller;

import com.rowland.engineering.shortner.entity.UrlMapping;
import com.rowland.engineering.shortner.service.UrlMappingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequestMapping("/api/v1/redirect")
@RequiredArgsConstructor
public class RedirectController {

    private final UrlMappingService urlMappingService;

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        UrlMapping urlMapping =  urlMappingService.getOriginalUrl(shortUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", urlMapping.getOriginalUrl());
        return ResponseEntity.status(302).headers(headers).build();
    }
}
