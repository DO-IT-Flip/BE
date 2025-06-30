package com.DoIt2.Flip.domain.schedule.repository;

import com.DoIt2.Flip.domain.schedule.entity.Schedule;
import com.DoIt2.Flip.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.time.LocalDate;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    // 사용자 기준 스케줄 전체 조회
    List<Schedule> findByUser(User user);

    // 날짜별 스케줄 조회
    List<Schedule> findByUserAndStartDate(User user, LocalDate startDate);
    // 키워드 필터 조회
    List<Schedule> findByUserAndTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrParticipantsContainingIgnoreCase(
            User user, String title, String location, String participants
    );


}
