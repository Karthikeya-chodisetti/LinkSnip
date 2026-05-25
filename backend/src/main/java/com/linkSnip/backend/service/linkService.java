package com.linkSnip.backend.service;

import com.linkSnip.backend.entity.link;
import com.linkSnip.backend.repository.linkRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class linkService {

    private final linkRepository repo;

    public linkService(linkRepository repo) {
        this.repo = repo;
    }

    public link createShortLink(String url) {

        String code;

        do {
            code = generateCode();
        } while (repo.findByShortCode(code).isPresent());

        link link = new link();

        link.setOriginalUrl(url);
        link.setShortCode(code);
        link.setCreatedAt(LocalDateTime.now());

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
                .orElseThrow(() -> new RuntimeException("Link not found"));

        return link.getOriginalUrl();
    }
}