package com.linkSnip.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor

public class link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 2000)
    private String originalUrl;

    @Column(unique = true)
    private String shortCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Long clicks = 0L;

    private LocalDateTime createdAt;

    private LocalDateTime expiryAt;

    private boolean expired;
}
