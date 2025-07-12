package com.DoIt2.Flip.domain.auth.service;

import com.DoIt2.Flip.global.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    private static final String REFRESH_KEY_PREFIX = "RT:"; // RefreshToken prefix
    private static final String BLACKLIST_KEY_PREFIX = "Blacklist:";

    public void saveRefreshToken(String userId, String refreshToken, long expirationMillis) {
        String key = REFRESH_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken, expirationMillis, TimeUnit.MILLISECONDS);
    }

    public String getRefreshToken(String userId) {
        return redisTemplate.opsForValue().get(REFRESH_KEY_PREFIX + userId);
    }

    public void deleteRefreshToken(String userId) {
        redisTemplate.delete(REFRESH_KEY_PREFIX + userId);
    }

    public boolean existsRefreshToken(String userId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(REFRESH_KEY_PREFIX + userId));
    }

    public void saveBlackListAccessToken(String accessToken) {
        long TTL = jwtUtil.getRemainingTime(accessToken);

        if(TTL > 0){
            String key = BLACKLIST_KEY_PREFIX + accessToken;
            redisTemplate.opsForValue().set(key, "true", TTL, TimeUnit.MILLISECONDS);
        }
    }

    public String getBlackListAccessToken(String accessToken) {
        return redisTemplate.opsForValue().get(BLACKLIST_KEY_PREFIX + accessToken);
    }

    public void deleteBlackListAccessToken(String accessToken) {
        redisTemplate.delete(BLACKLIST_KEY_PREFIX + accessToken);
    }

    public boolean existsBlackListAccessToken(String accessToken) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_KEY_PREFIX + accessToken));
    }
}
