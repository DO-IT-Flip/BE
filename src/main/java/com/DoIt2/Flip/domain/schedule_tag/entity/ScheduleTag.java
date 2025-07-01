package com.DoIt2.Flip.domain.schedule_tag.entity;

import com.DoIt2.Flip.domain.schedule.entity.Schedule;
import com.DoIt2.Flip.domain.tag.entity.Tag;
import com.DoIt2.Flip.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;

@Entity
@Table(name = "schedule_tag")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleTag {

    @EmbeddedId
    private ScheduleTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("scheduleId")
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    public ScheduleTag(Schedule schedule, Tag tag, User user) {
        this.schedule = schedule;
        this.tag = tag;
        this.user = user;
        this.id = new ScheduleTagId(schedule.getScheduleId(), tag.getTagId());
    }
}
