package com.DoIt2.Flip.domain.schedule_tag.repository;

import com.DoIt2.Flip.domain.schedule_tag.entity.ScheduleTag;
import com.DoIt2.Flip.domain.schedule_tag.entity.ScheduleTagId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleTagRepository extends JpaRepository<ScheduleTag, ScheduleTagId> {

    List<ScheduleTag> findBySchedule_ScheduleId(Long scheduleId);

    List<ScheduleTag> findByTag_TagId(Long tagId);

    void deleteBySchedule_ScheduleId(Long scheduleId);
}
