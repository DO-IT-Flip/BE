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
import com.DoIt2.Flip.domain.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final IconRepository iconRepository;
    private final TagRepository tagRepository;
    private final ScheduleTagRepository scheduleTagRepository;
    private final UserService userService;

    public ScheduleService(ScheduleRepository scheduleRepository,
                           IconRepository iconRepository,
                           TagRepository tagRepository,
                           ScheduleTagRepository scheduleTagRepository,
                           UserService userService) {
        this.scheduleRepository = scheduleRepository;
        this.iconRepository = iconRepository;
        this.tagRepository = tagRepository;
        this.scheduleTagRepository = scheduleTagRepository;
        this.userService = userService;
    }

    @Transactional
    public ScheduleResponse createSchedule(String userId, ScheduleRequest request) {
        User user = userService.getById(userId); // ✅ 진짜 유저 객체 조회

        Icon icon = null;
        if (request.getIconId() != null) {
            icon = iconRepository.findById(request.getIconId()).orElse(null);
        }

        String color = (request.getColor() != null) ? request.getColor() : "gray";

        Schedule schedule = Schedule.builder()
                .user(user)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(OffsetDateTime.of(request.getStartDate(), request.getStartTime(), OffsetDateTime.now().getOffset()))
                .endTime(OffsetDateTime.of(request.getEndDate(), request.getEndTime(), OffsetDateTime.now().getOffset()))
                .isRepeat(request.isRepeat())
                .title(request.getTitle())
                .location(request.getLocation())
                .participants(request.getParticipants())
                .color(color)
                .icon(icon)
                .build();

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
                }).toList();

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
        UUID uuid = UUID.fromString(userId);
        List<Schedule> schedules = scheduleRepository.findByUser_Id(uuid);
        return mapToScheduleResponses(schedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByDate(String userId, int year, int month, int day) {
        UUID uuid = UUID.fromString(userId);
        LocalDate date = LocalDate.of(year, month, day);
        List<Schedule> schedules = scheduleRepository.findByUser_IdAndStartDate(uuid, date);
        return mapToScheduleResponses(schedules);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> searchSchedules(String userId, String keyword) {
        UUID uuid = UUID.fromString(userId);
        List<Schedule> schedules = scheduleRepository.searchByKeyword(uuid, keyword);
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
        User user = userService.getById(userId); // ✅ 진짜 유저 객체 조회

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(user.getId())) {
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
        User user = userService.getById(userId);

        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));

        if (!schedule.getUser().getId().equals(user.getId())) {
            throw new IllegalStateException("본인의 일정만 삭제할 수 있습니다.");
        }

        scheduleRepository.delete(schedule);
    }
}
