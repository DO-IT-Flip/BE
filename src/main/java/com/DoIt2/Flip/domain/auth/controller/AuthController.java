package com.DoIt2.Flip.domain.auth.controller;

import com.DoIt2.Flip.domain.auth.dto.SignupRequest;
import com.DoIt2.Flip.domain.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth API", description = "유저 인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    // Swagger 등록을 위한 가짜 API (실제는 필터를 통해 인증)
    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인을 시도합니다. JWT 토큰을 반환합니다.")
    @PostMapping("/login")
    public void login() {}

    @Operation(summary = "회원가입", description = "유저 정보를 바탕으로 DB에 저장하여 회원가입합니다.")
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request){
        authService.signup(request);
        return ResponseEntity.ok("회원가입 성공");
    }

    // Swagger 등록을 위한 가짜 API (실제는 필터를 통해 인증)
    @Operation(summary = "로그아웃", description = "리프레시 토큰을 만료시키고 로그아웃 처리합니다.")
    @PostMapping("/logout")
    public void logout() {}

    @Operation(summary = "Access token 재발급", description = "Access token 만료 시 Refresh token 으로 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(authService.reissue(request, response));
    }
}
