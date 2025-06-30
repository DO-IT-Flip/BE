package com.DoIt2.Flip.global.jwt;

import com.DoIt2.Flip.domain.auth.dto.CustomUserDetails;
import com.DoIt2.Flip.domain.user.entity.User;
import com.DoIt2.Flip.domain.user.enums.Role;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        /// 토큰 검증
        // 1. request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");

        // 2. Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response); // 다음 필터로 넘겨줌

            return;
        }

        // 3. 접두사 Bearer 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];

        // 4. 토큰 소멸 시간 검증
        try {
            jwtUtil.isExpired(token);
        } catch(ExpiredJwtException e) {

            // response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 4. 토큰이 access 인지 확인
        String category = jwtUtil.getCategory(token);
        if (!category.equals("access token")) {

            // response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            // response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        /// SecurityContextHolder 라는 Secrity 세션에 유저 정보를 일시적으로 저장 (특정 admin 경로나 유저 정보를 요구하는 경로 진행 가능)
        // 5. 토큰에서 username과 role 획득해 유저 정보 획득
        String id = jwtUtil.getUserId(token);
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        User user = User.builder()
                .id(UUID.fromString(id))
                .email(username)
                .role(Role.valueOf(role))
                .build();

        // 6. UserDetails에 회원 정보 객체 담기
        CustomUserDetails customUserDetails = new CustomUserDetails(user);

        // 7. 스프링 시큐리티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        // 8. 세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 9. 다음 필터로 넘겨줌
        filterChain.doFilter(request, response);
    }
}
