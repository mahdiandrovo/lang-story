package com.langstory.auth.service;

import com.langstory.auth.entity.RefreshTokenEntity;
import com.langstory.auth.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Transactional
    public String createRefreshToken(UUID userId) {
        // delete old tokens for this user first
        refreshTokenRepository.deleteByUserId(userId);

        // create new refresh token
        RefreshTokenEntity refreshToken = new RefreshTokenEntity();
        refreshToken.setToken(jwtService.generateRefreshToken());
        refreshToken.setUserId(userId);
        refreshToken.setExpiresAt(Instant.now().plus(7, ChronoUnit.DAYS)); // 7 days
        refreshToken.setRevoked(false);

        refreshTokenRepository.save(refreshToken);

        return refreshToken.getToken();
    }

    public RefreshTokenEntity validateRefreshToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if (refreshToken.isRevoked()) {
            throw new RuntimeException("Refresh token is revoked");
        }

        if (refreshToken.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token is expired");
        }

        return refreshToken;
    }

    public void revokeRefreshToken(String token) {
        RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }
}
