package com.DoIt2.Flip.domain.schedule_tag.repository;

import com.DoIt2.Flip.domain.schedule_tag.entity.ScheduleTag;
import com.DoIt2.Flip.domain.schedule_tag.entity.ScheduleTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTagRepository extends JpaRepository<ScheduleTag, ScheduleTagId> {

    // 특정 스케줄에 연결된 모든 태그 조회
    List<ScheduleTag> findBySchedule_ScheduleId(Long scheduleId);

    // 특정 태그에 연결된 모든 스케줄 조회
    List<ScheduleTag> findByTag_TagId(Long tagId);

    // 스케줄 ID로 연결 정보 일괄 삭제
    void deleteBySchedule_ScheduleId(Long scheduleId);
}
