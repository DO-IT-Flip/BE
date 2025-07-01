package com.DoIt2.Flip.domain.schedule.repository;

import com.DoIt2.Flip.domain.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // ✅ 사용자 기준 전체 스케줄 조회
    List<Schedule> findByUser_Id(UUID userId);

    // ✅ 사용자 + 날짜별 조회
    List<Schedule> findByUser_IdAndStartDate(UUID userId, LocalDate startDate);

    // ✅ 사용자 + 키워드 검색 (정확한 OR 조건을 위해 @Query 사용)
    @Query("""
        SELECT s FROM Schedule s
        WHERE s.user.id = :userId AND (
            LOWER(s.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.location) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
            LOWER(s.participants) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    List<Schedule> searchByKeyword(@Param("userId") UUID userId, @Param("keyword") String keyword);
}
