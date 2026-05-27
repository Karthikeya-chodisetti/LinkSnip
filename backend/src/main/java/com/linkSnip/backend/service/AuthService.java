package com.linkSnip.backend.service;

import com.linkSnip.backend.dto.*;
import com.linkSnip.backend.entity.User;
import com.linkSnip.backend.exception.CustomException;
import com.linkSnip.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UserRepository repo,
            PasswordEncoder encoder,
            JwtService jwtService) {

        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    public String register(RegisterRequest request) {

        if (repo.findByEmail(request.getEmail()).isPresent()) {
            throw new CustomException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(encoder.encode(request.getPassword()));

        repo.save(user);

        return "User Registered Successfully";
    }

    public AuthResponse login(LoginRequest request) {

        User user = repo.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("Invalid Email"));

        if (!encoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomException("Invalid Password");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}