package com.DoIt2.Flip.domain.schedule.dto;

import com.DoIt2.Flip.domain.tag.DTO.TagResponse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private LocalDate startDate;
    private LocalTime startTime;
    private String color;
    private List<TagResponse> tags; // ✅ 태그 응답 추가

    // 기본 생성자
    public ScheduleResponse() {}

    // 전체 생성자
    public ScheduleResponse(Long scheduleId, String title, LocalDate startDate,
                            LocalTime startTime, String color, List<TagResponse> tags) {
        this.scheduleId = scheduleId;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.color = color;
        this.tags = tags;
    }

    // Getter & Setter
    public Long getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Long scheduleId) {
        this.scheduleId = scheduleId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public List<TagResponse> getTags() {
        return tags;
    }

    public void setTags(List<TagResponse> tags) {
        this.tags = tags;
    }
}
