package com.linkSnip.backend.controller;

import com.linkSnip.backend.dto.CreateLinkRequest;
import com.linkSnip.backend.dto.LinkAnalyticsResponse;
import com.linkSnip.backend.dto.LinkResponse;
import com.linkSnip.backend.entity.link;
import com.linkSnip.backend.service.linkService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;

@RestController
@RequestMapping("/api/links")
@CrossOrigin("*")
public class linkController {

    private final linkService service;

    public linkController(linkService service) {
        this.service = service;
    }

    @PostMapping
    public LinkResponse create(@RequestBody CreateLinkRequest request) {

        link link = service.createShortLink(
                request.getUrl(),
                request.getCustomAlias(),
                request.getTtlValue(),
                request.getTtlUnit());
        return new LinkResponse(
                "http://localhost:8080/api/links/" + link.getShortCode());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        String url = service.getOriginalUrl(code);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }

    @GetMapping("/{code}/analytics")
    public ResponseEntity<LinkAnalyticsResponse> analytics(@PathVariable String code) {

        return ResponseEntity.ok(service.getAnalytics(code));
    }
}
