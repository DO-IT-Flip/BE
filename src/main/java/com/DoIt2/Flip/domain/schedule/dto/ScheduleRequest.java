package com.DoIt2.Flip.domain.schedule.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class ScheduleRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private boolean isRepeat;
    private String title;
    private String location;
    private String participants;

    // ✅ 태그 존재 여부 (true면 tags 리스트 사용)
    private boolean isExistTag;

    // 태그 이름 리스트
    private List<String> tags;

    // 태그가 없을 경우 수동 지정할 색상, 아이콘
    private String color;
    private Long iconId;

    public ScheduleRequest() {}

    public ScheduleRequest(LocalDate startDate, LocalDate endDate,
                           LocalTime startTime, LocalTime endTime, boolean isRepeat,
                           String title, String location, String participants,
                           boolean isExistTag, List<String> tags,
                           String color, Long iconId) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isRepeat = isRepeat;
        this.title = title;
        this.location = location;
        this.participants = participants;
        this.isExistTag = isExistTag;
        this.tags = tags;
        this.color = color;
        this.iconId = iconId;
    }

    // Getter & Setter
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public LocalTime getStartTime() { return startTime; }
    public void setStartTime(LocalTime startTime) { this.startTime = startTime; }

    public LocalTime getEndTime() { return endTime; }
    public void setEndTime(LocalTime endTime) { this.endTime = endTime; }

    public boolean isRepeat() { return isRepeat; }
    public void setRepeat(boolean repeat) { isRepeat = repeat; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getParticipants() { return participants; }
    public void setParticipants(String participants) { this.participants = participants; }

    public boolean isExistTag() { return isExistTag; }
    public void setExistTag(boolean existTag) { isExistTag = existTag; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Long getIconId() { return iconId; }
    public void setIconId(Long iconId) { this.iconId = iconId; }
}
