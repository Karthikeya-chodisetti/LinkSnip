package com.linkSnip.backend.scheduler;

import com.linkSnip.backend.repository.linkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LinkCleanupScheduler {

    private final linkRepository repo;

    @Scheduled(fixedRate = 3600000)
    public void cleanupExpiredLinks() {

        var expiredLinks = repo.findByExpiryAtBefore(LocalDateTime.now());

        repo.deleteAll(expiredLinks);

    }
}