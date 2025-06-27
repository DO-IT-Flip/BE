package com.DoIt2.Flip.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "RT:"; // RefreshToken prefix

    public void saveRefreshToken(String userId, String refreshToken, long expirationMillis) {
        String key = KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, expirationMillis, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(KEY_PREFIX + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(KEY_PREFIX + userId);
    }

    public boolean existsRefreshToken(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(KEY_PREFIX + userId));
    }
}
