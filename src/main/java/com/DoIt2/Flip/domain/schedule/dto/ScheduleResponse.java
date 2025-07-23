package com.DoIt2.Flip.domain.schedule.dto;

import com.DoIt2.Flip.domain.schedule.entity.Schedule;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {

    private Long scheduleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private OffsetDateTime startTime;
    private OffsetDateTime endTime;
    private String title;
    private String location;
    private String participants;
    private String color;
    private Long iconId;
    private Long tagId;
    private boolean isRepeat;
    private boolean isExistTag;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public static ScheduleResponse from(Schedule schedule) {
        return ScheduleResponse.builder()
                .scheduleId(schedule.getScheduleId())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .title(schedule.getTitle())
                .location(schedule.getLocation())
                .participants(schedule.getParticipants())
                .color(schedule.isExistTag() && schedule.getTag() != null
                        ? schedule.getTag().getColor()
                        : schedule.getColor())
                .iconId(schedule.isExistTag() && schedule.getTag() != null
                        ? (schedule.getTag().getIcon() != null ? schedule.getTag().getIcon().getIconId() : null)
                        : (schedule.getIcon() != null ? schedule.getIcon().getIconId() : null))
                .tagId(schedule.getTag() != null ? schedule.getTag().getTagId() : null)
                .isRepeat(schedule.isRepeat())
                .isExistTag(schedule.isExistTag())
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
}
