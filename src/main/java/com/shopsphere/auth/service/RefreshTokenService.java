package com.shopsphere.auth.service;


import com.shopsphere.auth.entity.RefreshToken;
import com.shopsphere.user.entity.User;
import com.shopsphere.auth.repository.RefreshTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public RefreshToken createRefreshToken(User user) {

        // Remove old refresh token for this user
        repository.deleteByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .user(user)
                .expiryDate(
                        Instant.now().plusSeconds(7 * 24 * 60 * 60)
                )
                .build();

        return repository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {

        if (token.getExpiryDate().isBefore(Instant.now())) {

            repository.delete(token);

            throw new RuntimeException(
                    "Refresh token has expired. Please login again."
            );
        }

        return token;
    }

    public RefreshToken findByToken(String token) {

        return repository.findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found")
                );
    }
}