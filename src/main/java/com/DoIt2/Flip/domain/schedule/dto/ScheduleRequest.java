package com.DoIt2.Flip.domain.schedule.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String title;
    private String location;
    private String participants;

    // 반복 일정 여부
    @JsonProperty("isRepeat")
    private boolean isRepeat;

    //  태그 존재 여부
    @JsonProperty("isExistTag")
    private boolean isExistTag;

    //  태그가 존재하면 사용되는 필드
    private Long tagId;

    //  태그가 없을 경우 수동 입력용
    private String color;
    private Long iconId;
}