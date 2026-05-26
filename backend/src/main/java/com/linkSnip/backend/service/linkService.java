package com.linkSnip.backend.service;

import com.linkSnip.backend.entity.link;
import com.linkSnip.backend.exception.CustomException;
import com.linkSnip.backend.repository.linkRepository;
import com.linkSnip.backend.dto.LinkAnalyticsResponse;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class linkService {

    private final linkRepository repo;

    public linkService(linkRepository repo) {
        this.repo = repo;
    }

    public link createShortLink(String url, String customAlias, Integer ttlValue, String ttlUnit) {

        if (url == null || (!url.startsWith("http://") && !url.startsWith("https://"))) {
            throw new CustomException("Invalid URL");
        }

        String code;

        if (customAlias != null && !customAlias.isBlank()) {

            if (repo.findByShortCode(customAlias).isPresent()) {
                throw new CustomException("Alias already exists");
            }

            code = customAlias;

        } else {

            do {
                code = generateCode();
            } while (repo.findByShortCode(code).isPresent());
        }

        link link = new link();

        link.setOriginalUrl(url);
        link.setShortCode(code);
        link.setCreatedAt(LocalDateTime.now());

        if (ttlValue != null && ttlUnit != null) {

            LocalDateTime expiryTime;

            switch (ttlUnit.toUpperCase()) {

                case "MINUTES":
                    expiryTime = LocalDateTime.now().plusMinutes(ttlValue);
                    break;

                case "HOURS":
                    expiryTime = LocalDateTime.now().plusHours(ttlValue);
                    break;

                case "DAYS":
                    expiryTime = LocalDateTime.now().plusDays(ttlValue);
                    break;

                default:
                    throw new CustomException("Invalid TTL Unit");
            }
            link.setExpiryAt(expiryTime);
        }
        return repo.save(link);
    }

    private String generateCode() {

        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder sb = new StringBuilder();

        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }

    public String getOriginalUrl(String code) {

        link link = repo.findByShortCode(code)
                .orElseThrow(() -> new CustomException("Link not found"));

        if (link.getExpiryAt() != null &&
                link.getExpiryAt().isBefore(LocalDateTime.now())) {

            throw new CustomException("Link expired");
        }

        link.setClicks(link.getClicks() + 1);
        repo.save(link);

        return link.getOriginalUrl();
    }

    public LinkAnalyticsResponse getAnalytics(String code) {

        link link = repo.findByShortCode(code)
                .orElseThrow(() -> new CustomException("Link not found"));

        LinkAnalyticsResponse response = new LinkAnalyticsResponse();

        response.setOriginalUrl(link.getOriginalUrl());
        response.setShortCode(link.getShortCode());
        response.setClicks(link.getClicks());
        response.setCreatedAt(link.getCreatedAt());
        response.setExpiryAt(link.getExpiryAt());

        boolean expired = link.getExpiryAt() != null && link.getExpiryAt().isBefore(LocalDateTime.now());

        response.setExpired(expired);

        return response;
    }
}