package com.DoIt2.Flip.domain.schedule.entity;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import com.DoIt2.Flip.domain.schedule_tag.entity.ScheduleTag;
import com.DoIt2.Flip.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long scheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime startTime;

    @Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private OffsetDateTime endTime;

    private boolean isRepeat;
    private String title;
    private String location;
    private String participants;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon;

    @Column(name = "created_at", insertable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<ScheduleTag> scheduleTags = new ArrayList<>();
}
