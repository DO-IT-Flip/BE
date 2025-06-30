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
import org.springframework.http.HttpStatus;
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

    public ResponseEntity reissue(HttpServletRequest request, HttpServletResponse response) {
        //get refresh token
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {

            if (cookie.getName().equals("refresh")) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            //response status code
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        //expired check
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {

            //response status code
            return new ResponseEntity<>("refresh token expired", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh 인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String userId = jwtUtil.getUserId(refresh);
        String username = jwtUtil.getUsername(refresh);
        String role = jwtUtil.getRole(refresh);

        String redisRefreshToken = redisTokenService.getRefreshToken(userId);

        if (!refresh.equals(redisRefreshToken)) {
            return new ResponseEntity<>("no matched refresh token", HttpStatus.BAD_REQUEST);
        }

        // make new JWT
        String newAccess = jwtUtil.createJwt("access token", userId, username, role, (long) EnvLoader.getInt("JWT_ACCESS_EXPIRATION", 600000));
        String newRefresh = jwtUtil.createJwt("refresh token", userId, username, role, (long) EnvLoader.getInt("JWT_REFRESH_EXPIRATION", 86400000));

        // response
        response.setHeader("access", newAccess);
        response.addCookie(cookieUtil.createCookie("refresh", newRefresh));

        // refresh 토큰 저장
        redisTokenService.deleteRefreshToken(userId);
        redisTokenService.saveRefreshToken(userId, refresh, (long) EnvLoader.getInt("JWT_REFRESH_EXPIRATION", 86400000));

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
