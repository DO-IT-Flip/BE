package com.DoIt2.Flip.domain.schedule.dto;

import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private String color;
    private List<TagResponse> tags; // 태그 응답 포함
}
