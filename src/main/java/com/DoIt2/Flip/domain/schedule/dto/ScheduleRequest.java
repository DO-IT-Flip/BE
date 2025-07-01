package com.DoIt2.Flip.domain.schedule.dto;

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
    private boolean repeat;
    private String title;
    private String location;
    private String participants;

    //  태그 존재 여부
    private boolean existTag;

    //  태그가 존재하면 사용되는 필드
    private List<String> tags;

    //  태그가 없을 경우 수동 입력용
    private String color;
    private Long iconId;
}
