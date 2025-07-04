package com.DoIt2.Flip.global.jwt;

import com.DoIt2.Flip.domain.auth.service.RedisTokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RedisTokenService redisTokenService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 로그아웃 API 경로 + POST 요청이 아닐 경우 필터 패스
        if (!request.getRequestURI().equals("/api/auth/logout") || !request.getMethod().equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 1. 쿠키에서 refresh 토큰 추출
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("refresh")) {
                    refresh = cookie.getValue();
                    break;
                }
            }
        }

        // refresh 토큰이 없으면 400
        if (refresh == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // refresh 토큰 만료 여부 확인
        try {
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // category가 refresh인지 검증
        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 2. Authorization 헤더에서 access 토큰 추출
        String access = null;
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            access = authHeader.substring(7);
        }

        // access 토큰 블랙리스트 등록
        if (access != null && "access".equals(jwtUtil.getCategory(access))) {
            redisTokenService.saveBlackListAccessToken(access);
        }

        // 3. refresh 토큰의 사용자 ID 추출 및 존재 확인
        String userId = jwtUtil.getUserId(refresh);
        if (!redisTokenService.existsRefreshToken(userId)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // 4. Redis에서 refresh 토큰 삭제
        redisTokenService.deleteRefreshToken(userId);

        // 5. refresh 쿠키 삭제
        Cookie cookie = new Cookie("refresh", null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        // 6. 정상 로그아웃 응답
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
