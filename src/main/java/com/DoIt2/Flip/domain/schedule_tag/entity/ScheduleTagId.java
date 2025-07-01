package com.DoIt2.Flip.domain.schedule_tag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ScheduleTagId implements Serializable {

    @Column(name = "schedule_id")
    private Long scheduleId;

    @Column(name = "tag_id")
    private Long tagId;
}
