package com.DoIt2.Flip.global.jwt;

import com.DoIt2.Flip.domain.auth.service.RedisTokenService;
import com.DoIt2.Flip.domain.user.entity.User;
import com.DoIt2.Flip.domain.user.service.UserService;
import com.DoIt2.Flip.global.cookie.CookieUtil;
import com.DoIt2.Flip.global.env.EnvLoader;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Collection;
import java.util.Iterator;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisTokenService redisTokenService;
    private final UserService userService;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        // 클라이언트 요청에서 username, password 추출
        String username = obtainUsername(request);
        String password = obtainPassword(request);

        // 스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        // token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }


    // 로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        // 1. User 정보 가져오기
        String username = authentication.getName();
        User user = userService.getByUsername(username);

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // 2. token 생성
        String access = jwtUtil.createJwt("access token", user.getUserId().toString(), username, role, (long) EnvLoader.getInt("JWT_ACCESS_EXPIRATION", 600000));
        String refresh = jwtUtil.createJwt("refresh token", user.getUserId().toString(), username, role, (long) EnvLoader.getInt("JWT_REFRESH_EXPIRATION", 86400000));

        // 3. refresh 토큰 저장
        redisTokenService.saveRefreshToken(user.getUserId().toString(), refresh, (long) EnvLoader.getInt("JWT_REFRESH_EXPIRATION", 86400000));

        // 4. response의 Header에 담음 (HTTP 인증 방식은 RFC 7235 정의에 따라 아래 인증 헤더 형태를 따름)
        response.addHeader("Authorization", "Bearer " + access);
        response.addCookie(cookieUtil.createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }


    // 로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {

        // TODO: 커스텀 에러처리 고려
        // 401 응답 코드 반환
        response.setStatus(401);
    }
}