package com.DoIt2.Flip.domain.user.service;

import com.DoIt2.Flip.domain.user.entity.User;
import com.DoIt2.Flip.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getByUsername(String username){
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new EntityNotFoundException("해당 아이디의 유저를 찾을 수 없습니다."));
    }
}
