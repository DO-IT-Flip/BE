package com.DoIt2.Flip.domain.user.controller;

import com.DoIt2.Flip.domain.auth.dto.CustomUserDetails;
import com.DoIt2.Flip.domain.user.dto.UserInfoResponse;
import com.DoIt2.Flip.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "태그 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @Operation(summary = "내 정보 조회", description = "현재 접속 중인 유저의 정보를 JWT 토큰을 통해 반환합니다.")
    @GetMapping("/auth/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUserId()));
    }
}
