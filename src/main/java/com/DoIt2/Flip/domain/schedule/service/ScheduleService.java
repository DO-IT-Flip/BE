package com.DoIt2.Flip.domain.schedule.service;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import com.DoIt2.Flip.domain.icon.repository.IconRepository;
import com.DoIt2.Flip.domain.schedule.dto.ScheduleRequest;
import com.DoIt2.Flip.domain.schedule.dto.ScheduleResponse;
import com.DoIt2.Flip.domain.schedule.entity.Schedule;
import com.DoIt2.Flip.domain.schedule.repository.ScheduleRepository;
import com.DoIt2.Flip.domain.schedule_tag.entity.ScheduleTag;
import com.DoIt2.Flip.domain.schedule_tag.repository.ScheduleTagRepository;
import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import com.DoIt2.Flip.domain.tag.entity.Tag;
import com.DoIt2.Flip.domain.tag.repository.TagRepository;
import com.DoIt2.Flip.domain.user.entity.User;
import com.DoIt2.Flip.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;
    private final IconRepository iconRepository;
    private final TagRepository tagRepository;
    private final ScheduleTagRepository scheduleTagRepository;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           UserRepository userRepository,
                           IconRepository iconRepository,
                           TagRepository tagRepository,
                           ScheduleTagRepository scheduleTagRepository) {
        this.scheduleRepository = scheduleRepository;
        this.userRepository = userRepository;
        this.iconRepository = iconRepository;
        this.tagRepository = tagRepository;
        this.scheduleTagRepository = scheduleTagRepository;
    }

    @Transactional
    public ScheduleResponse createSchedule(String userId, ScheduleRequest request) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Icon icon = null;
        if (request.getIconId() != null) {
            icon = iconRepository.findById(request.getIconId()).orElse(null);
        }

        String color = (request.getColor() != null) ? request.getColor() : "gray";

        Schedule schedule = new Schedule();
        schedule.setUser(user);
        schedule.setStartDate(request.getStartDate());
        schedule.setEndDate(request.getEndDate());
        schedule.setStartTime(OffsetDateTime.of(request.getStartDate(), request.getStartTime(), OffsetDateTime.now().getOffset()));
        schedule.setEndTime(OffsetDateTime.of(request.getEndDate(), request.getEndTime(), OffsetDateTime.now().getOffset()));
        schedule.setRepeat(request.isRepeat());
        schedule.setTitle(request.getTitle());
        schedule.setLocation(request.getLocation());
        schedule.setParticipants(request.getParticipants());
        schedule.setColor(color);
        schedule.setIcon(icon);

        Schedule saved = scheduleRepository.save(schedule);

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));

                ScheduleTag scheduleTag = new ScheduleTag(saved, tag, user);
                scheduleTagRepository.save(scheduleTag);
            }
        }

        List<TagResponse> tagResponses = saved.getScheduleTags().stream()
                .map(st -> {
                    Tag tag = st.getTag();
                    return new TagResponse(tag.getTagId(), tag.getName(), tag.getColor(), tag.getIcon().getIconId());
                })
                .toList();

        return new ScheduleResponse(
                saved.getScheduleId(),
                saved.getTitle(),
                saved.getStartDate(),
                saved.getStartTime().toLocalTime(),
                saved.getColor(),
                tagResponses
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAllSchedules(String userId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Schedule> schedules = scheduleRepository.findByUser(user);

        return mapToScheduleResponses(schedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByDate(String userId, int year, int month, int day) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        LocalDate date = LocalDate.of(year, month, day);
        List<Schedule> schedules = scheduleRepository.findByUserAndStartDate(user, date);

        return mapToScheduleResponses(schedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> searchSchedules(String userId, String keyword) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        List<Schedule> schedules = scheduleRepository
                .findByUserAndTitleContainingIgnoreCaseOrLocationContainingIgnoreCaseOrParticipantsContainingIgnoreCase(
                        user, keyword, keyword, keyword
                );

        return mapToScheduleResponses(schedules);
    }

    private List<ScheduleResponse> mapToScheduleResponses(List<Schedule> schedules) {
        return schedules.stream().map(schedule -> {
            List<TagResponse> tagResponses = schedule.getScheduleTags().stream()
                    .map(st -> {
                        Tag tag = st.getTag();
                        return new TagResponse(tag.getTagId(), tag.getName(), tag.getColor(), tag.getIcon().getIconId());
                    }).toList();

            return new ScheduleResponse(
                    schedule.getScheduleId(),
                    schedule.getTitle(),
                    schedule.getStartDate(),
                    schedule.getStartTime().toLocalTime(),
                    schedule.getColor(),
                    tagResponses
            );
        }).toList();
    }

    @Transactional
    public ScheduleResponse updateSchedule(String userId, Long scheduleId, ScheduleRequest request) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(UUID.fromString(userId))) {
            throw new IllegalStateException("본인의 일정만 수정할 수 있습니다.");
        }

        scheduleTagRepository.deleteAll(schedule.getScheduleTags());
        schedule.getScheduleTags().clear();

        schedule.setTitle(request.getTitle());
        schedule.setStartDate(request.getStartDate());
        schedule.setEndDate(request.getEndDate());
        schedule.setStartTime(OffsetDateTime.of(request.getStartDate(), request.getStartTime(), OffsetDateTime.now().getOffset()));
        schedule.setEndTime(OffsetDateTime.of(request.getEndDate(), request.getEndTime(), OffsetDateTime.now().getOffset()));
        schedule.setRepeat(request.isRepeat());
        schedule.setLocation(request.getLocation());
        schedule.setParticipants(request.getParticipants());

        if (request.getColor() != null) schedule.setColor(request.getColor());
        if (request.getIconId() != null) {
            Icon icon = iconRepository.findById(request.getIconId()).orElse(null);
            schedule.setIcon(icon);
        }

        if (request.getTags() != null && !request.getTags().isEmpty()) {
            for (String tagName : request.getTags()) {
                Tag tag = tagRepository.findByName(tagName)
                        .orElseGet(() -> tagRepository.save(new Tag(tagName)));

                ScheduleTag scheduleTag = new ScheduleTag(schedule, tag, user);
                scheduleTagRepository.save(scheduleTag);
                schedule.getScheduleTags().add(scheduleTag);
            }
        }

        List<TagResponse> tagResponses = schedule.getScheduleTags().stream()
                .map(st -> {
                    Tag tag = st.getTag();
                    return new TagResponse(tag.getTagId(), tag.getName(), tag.getColor(), tag.getIcon().getIconId());
                }).toList();

        return new ScheduleResponse(
                schedule.getScheduleId(),
                schedule.getTitle(),
                schedule.getStartDate(),
                schedule.getStartTime().toLocalTime(),
                schedule.getColor(),
                tagResponses
        );
    }

    @Transactional
    public void deleteSchedule(String userId, Long scheduleId) {
        User user = userRepository.findById(UUID.fromString(userId))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(UUID.fromString(userId))) {
            throw new IllegalStateException("본인의 일정만 삭제할 수 있습니다.");
        }

        scheduleRepository.delete(schedule);
    }
}
