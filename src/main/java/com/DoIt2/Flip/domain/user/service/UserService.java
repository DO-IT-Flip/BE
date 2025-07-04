package com.DoIt2.Flip.domain.user.service;

import com.DoIt2.Flip.domain.user.dto.UserInfoResponse;
import com.DoIt2.Flip.domain.user.entity.User;
import com.DoIt2.Flip.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getById(String userId){

        return userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다."));
    }

    public User getByEmail(String email){

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다."));
    }

    public UserInfoResponse getMyInfo(String userId){

        User user = getById(userId);
        return UserInfoResponse.builder()
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .theme(user.getTheme())
                .build();
    }
}
