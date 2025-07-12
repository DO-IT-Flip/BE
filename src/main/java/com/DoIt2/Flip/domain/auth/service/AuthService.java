package com.DoIt2.Flip.domain.auth.service;

import com.DoIt2.Flip.domain.auth.dto.SignupRequest;
import com.DoIt2.Flip.domain.user.entity.User;
import com.DoIt2.Flip.domain.user.enums.Role;
import com.DoIt2.Flip.domain.user.enums.Theme;
import com.DoIt2.Flip.domain.user.repository.UserRepository;
import com.DoIt2.Flip.global.cookie.CookieUtil;
import com.DoIt2.Flip.global.env.EnvLoader;
import com.DoIt2.Flip.global.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisTokenService redisTokenService;
    private final UserRepository userRepository;

    public void signup(SignupRequest request) {

        // 1. 사용자 아이디 중복 체크
        if (userRepository.existsByEmail(request.getUsername())) {
            throw new IllegalAccessError("이미 존재하는 계정입니다.");
        }

        // 2. 비밀번호 암호화
        String encodedPassword = bCryptPasswordEncoder.encode(request.getPassword());

        User user = User.builder()
                .email(request.getUsername())
                .password(encodedPassword)
                .name(request.getName())
                .theme(Theme.black)
                .role(Role.USER)
                .build();

        userRepository.save(user);
    }

    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        // 1. Refresh Token 추출
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return ResponseEntity.badRequest().body("no cookies");

        String refresh = null;
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) return ResponseEntity.badRequest().body("refresh token null");

        // 2. Refresh 토큰 유효성 검사
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            return ResponseEntity.badRequest().body("refresh token expired");
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            return ResponseEntity.badRequest().body("invalid refresh token");
        }

        String userId = jwtUtil.getUserId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String redisRefresh = redisTokenService.getRefreshToken(userId);
        if (!refresh.equals(redisRefresh)) {
            return ResponseEntity.badRequest().body("no matched refresh token");
        }

        // 3. access token 추출 및 블랙리스트 처리
        String access = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            access = authHeader.substring(7);
        }

        if (access != null && "access".equals(jwtUtil.getCategory(access))) {
            redisTokenService.saveBlackListAccessToken(access);

        }

        // 4. 새 토큰 발급
        long accessExp = EnvLoader.getInt("JWT_ACCESS_EXPIRATION", 600000);
        long refreshExp = EnvLoader.getInt("JWT_REFRESH_EXPIRATION", 86400000);

        String newAccess = jwtUtil.createJwt("access", userId, username, role, accessExp);
        String newRefresh = jwtUtil.createJwt("refresh", userId, username, role, refreshExp);

        // 5. 응답 설정
        response.setHeader("access", "Bearer " + newAccess);
        response.addCookie(cookieUtil.createCookie("refresh", newRefresh));

        // 6. Redis refresh 갱신
        redisTokenService.deleteRefreshToken(userId);
        redisTokenService.saveRefreshToken(userId, newRefresh, refreshExp);

        return ResponseEntity.ok().build();
    }
}
