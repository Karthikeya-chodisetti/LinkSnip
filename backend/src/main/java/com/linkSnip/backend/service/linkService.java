package com.linkSnip.backend.service;

import com.linkSnip.backend.entity.link;
import com.linkSnip.backend.exception.CustomException;
import com.linkSnip.backend.repository.UserRepository;
import com.linkSnip.backend.repository.linkRepository;
import com.linkSnip.backend.dto.LinkAnalyticsResponse;
import com.linkSnip.backend.dto.LinkResponse;
import com.linkSnip.backend.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class linkService {

    private final linkRepository repo;
    private final UserRepository userRepo;

    public linkService(linkRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
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

        LocalDateTime expiryTime;

        if (ttlValue != null && ttlUnit != null) {

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

        } else {

            expiryTime = LocalDateTime.now().plusDays(30);
        }

        if (expiryTime.isAfter(LocalDateTime.now().plusDays(30))) {
            throw new CustomException("Expiry cannot exceed 30 days");
        }

        link.setExpiryAt(expiryTime);

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        link.setUser(user);

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

    public List<LinkAnalyticsResponse> getUserLinks(String email) {

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        List<link> links = repo.findByUser(user)
                .stream()
                .filter(link -> link.getExpiryAt() == null ||
                        link.getExpiryAt().isAfter(LocalDateTime.now()))
                .toList();

        return links.stream().map(this::mapToResponse).toList();
    }

    private LinkAnalyticsResponse mapToResponse(link link) {

        LinkAnalyticsResponse response = new LinkAnalyticsResponse();

        response.setId(link.getId());
        response.setOriginalUrl(link.getOriginalUrl());
        response.setShortCode(link.getShortCode());
        response.setClicks(link.getClicks());
        response.setCreatedAt(link.getCreatedAt());
        response.setExpiryAt(link.getExpiryAt());
        response.setExpired(link.isExpired());

        return response;
    }

    public void deleteLink(String code, String email) {

        link link = repo.findByShortCode(code)
                .orElseThrow(() -> new CustomException("Link not found"));

        if (!link.getUser().getEmail().equals(email)) {
            throw new CustomException("Unauthorized");
        }

        repo.delete(link);
    }

    public link getLinkEntity(String code) {

        return repo.findByShortCode(code)
                .orElseThrow(() -> new CustomException("Link not found"));
    }
}