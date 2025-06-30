package com.DoIt2.Flip.domain.user.controller;

import com.DoIt2.Flip.domain.auth.dto.CustomUserDetails;
import com.DoIt2.Flip.domain.user.dto.UserInfoResponse;
import com.DoIt2.Flip.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @GetMapping("/auth/me")
    public ResponseEntity<UserInfoResponse> getMyInfo(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(userService.getMyInfo(userDetails.getUserId()));
    }
}
