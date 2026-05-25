package com.linkSnip.backend.repository;

import com.linkSnip.backend.entity.link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface linkRepository extends JpaRepository<link, Long> {
    Optional<link> findByShortCode(String shortCode);
}