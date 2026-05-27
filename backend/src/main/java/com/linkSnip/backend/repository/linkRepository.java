package com.linkSnip.backend.repository;

import com.linkSnip.backend.entity.link;
import com.linkSnip.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.*;

public interface linkRepository extends JpaRepository<link, Long> {
    Optional<link> findByShortCode(String shortCode);

    List<link> findByUser(User user);

    List<link> findByExpiryAtBefore(LocalDateTime now);
}