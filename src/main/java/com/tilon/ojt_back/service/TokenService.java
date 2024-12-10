package com.tilon.ojt_back.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.tilon.ojt_back.domain.RefreshToken;
import com.tilon.ojt_back.dao.RefreshTokenRepository;

@Service
public class TokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public TokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    // 리프레시 토큰 저장
    public void saveRefreshToken(String userId, String token) {
        RefreshToken refreshToken = new RefreshToken(userId, token);
        refreshTokenRepository.save(refreshToken);
    }

    // 리프레시 토큰 조회
    public String getRefreshToken(String userId) {
        return refreshTokenRepository.findById(userId)
                .map(RefreshToken::getToken)
                .orElse(null);
    }
}
