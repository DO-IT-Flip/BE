package com.DoIt2.Flip.domain.schedule_tag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ScheduleTagId implements Serializable {

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "tag_id")
    private Long tagId;

    public ScheduleTagId() {}

    public ScheduleTagId(Long scheduleId, Long tagId) {
        this.scheduleId = scheduleId;
        this.tagId = tagId;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getTagId() {
        return tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ScheduleTagId)) return false;
        ScheduleTagId that = (ScheduleTagId) o;
        return Objects.equals(scheduleId, that.scheduleId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(scheduleId, tagId);
    }
}
