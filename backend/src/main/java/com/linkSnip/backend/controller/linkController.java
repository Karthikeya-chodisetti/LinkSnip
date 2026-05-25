package com.linkSnip.backend.controller;

import com.linkSnip.backend.entity.link;
import com.linkSnip.backend.service.linkService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;

import java.util.Map;

@RestController
@RequestMapping("/api/links")
@CrossOrigin("*")
public class linkController {

    private final linkService service;

    public linkController(linkService service) {
        this.service = service;
    }

    @PostMapping
    public Map<String, String> create(@RequestBody Map<String, String> body) {

        link link = service.createShortLink(body.get("url"));

        return Map.of("shortUrl", "http://localhost:8080/api/links/" + link.getShortCode());
    }

    @GetMapping("/{code}")
    public ResponseEntity<Void> redirect(@PathVariable String code) {

        String url = service.getOriginalUrl(code);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, url)
                .build();
    }
}
