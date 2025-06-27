package com.DoIt2.Flip.global.config;

import com.DoIt2.Flip.domain.auth.service.RedisTokenService;
import com.DoIt2.Flip.domain.user.service.UserService;
import com.DoIt2.Flip.global.cookie.CookieUtil;
import com.DoIt2.Flip.global.jwt.JwtFilter;
import com.DoIt2.Flip.global.jwt.JwtUtil;
import com.DoIt2.Flip.global.jwt.LoginFilter;
import com.DoIt2.Flip.global.jwt.CustomLogoutFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final RedisTokenService redisTokenService;
    private final UserService userService;


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.cors((cors) -> cors.configurationSource(new CorsConfigurationSource() {
            @Override
            public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                CorsConfiguration configuration = new CorsConfiguration();

                configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000")); // 허용할 주소
                configuration.setAllowedMethods(Collections.singletonList("*")); // 허용할 HTTP methods
                configuration.setAllowCredentials(true);
                configuration.setAllowedHeaders(Collections.singletonList("*")); // 허용할 Header
                configuration.setMaxAge(3600L); // 허용 시간

                configuration.setExposedHeaders(Collections.singletonList("Authorization")); // "Authorization" Header 허용

                return configuration;
            }
        }));

        // csrf disable : JWT 방식은 Stateless 상태로 관리하므로 csrf에 대한 공격을 방어하지 않아도 됌
        http.csrf((auth) -> auth.disable());

        // From 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        // http basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        // 경로별 인가 작업
        http.authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login", "/", "/join").permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN")
                        .requestMatchers("/reissue").permitAll()
                        .anyRequest().authenticated());

        // 직접 만든 JWTFilter 추가 (첫 번째 인자는 생성한 필터, 두 번째 인자는 필터를 넣을 위치)
        http.addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class);

        // 직접 만든 LoginFilter 추가
        http.addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, cookieUtil, redisTokenService, userService),
                UsernamePasswordAuthenticationFilter.class);

        // 직접 만든 LogoutFilter 추가
        http.addFilterBefore(new CustomLogoutFilter(jwtUtil, redisTokenService), LogoutFilter.class);

        // 세션 설정 (중요)
        http.sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        return http.build();
    }
}
