package com.DoIt2.Flip.domain.icon.repository;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IconRepository extends JpaRepository<Icon, Long> {
    // 필요한 경우 커스텀 쿼리 추가
}
