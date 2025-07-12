package com.DoIt2.Flip.domain.schedule.service;

import com.DoIt2.Flip.domain.icon.repository.IconRepository;
import com.DoIt2.Flip.domain.schedule.dto.ScheduleRequest;
import com.DoIt2.Flip.domain.schedule.dto.ScheduleResponse;
import com.DoIt2.Flip.domain.schedule.entity.Schedule;
import com.DoIt2.Flip.domain.schedule.repository.ScheduleRepository;
import com.DoIt2.Flip.domain.tag.repository.TagRepository;
import com.DoIt2.Flip.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final IconRepository iconRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public Schedule getById(Long scheduleId){
        return scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("해당 일정이 존재하지 않습니다."));
    }

    @Transactional
    public ScheduleResponse createSchedule(String userId, ScheduleRequest request) {
        Schedule schedule = Schedule.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userId)))
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startTime(OffsetDateTime.of(request.getStartDate(), request.getStartTime(), OffsetDateTime.now().getOffset()))
                .endTime(OffsetDateTime.of(request.getEndDate(), request.getEndTime(), OffsetDateTime.now().getOffset()))
                .title(request.getTitle())
                .location(request.getLocation())
                .participants(request.getParticipants())
                .color(request.getColor()) // DB에 초기값이 설정되어 있으므로 추가 로직 필요 없음
                .isRepeat(request.isRepeat())
                .isExistTag(request.isExistTag())
                .build();

        if (request.isExistTag()){
            schedule.setTag(tagRepository.getReferenceById(request.getTagId()));
        } else {
            schedule.setIcon(iconRepository.getReferenceById(request.getIconId()));
        }

        Schedule saved = scheduleRepository.save(schedule);
        return ScheduleResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getAllSchedules(String userId) {
        UUID uuid = UUID.fromString(userId);
        return scheduleRepository.findByUser_Id(uuid).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> getSchedulesByDate(String userId, int year, int month, int day) {
        UUID uuid = UUID.fromString(userId);
        LocalDate date = LocalDate.of(year, month, day);
        return scheduleRepository.findByUser_IdAndStartDate(uuid, date).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ScheduleResponse> searchSchedules(String userId, String keyword) {
        UUID uuid = UUID.fromString(userId);
        return scheduleRepository.searchByKeyword(uuid, keyword).stream()
                .map(ScheduleResponse::from)
                .toList();
    }

    @Transactional
    public ScheduleResponse updateSchedule(String userId, Long scheduleId, ScheduleRequest request) {
        Schedule schedule = getById(scheduleId);

        if (!schedule.getUser().getId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        schedule.setTitle(request.getTitle());
        schedule.setStartDate(request.getStartDate());
        schedule.setEndDate(request.getEndDate());
        schedule.setStartTime(OffsetDateTime.of(request.getStartDate(), request.getStartTime(), OffsetDateTime.now().getOffset()));
        schedule.setEndTime(OffsetDateTime.of(request.getEndDate(), request.getEndTime(), OffsetDateTime.now().getOffset()));
        schedule.setRepeat(request.isRepeat());
        schedule.setLocation(request.getLocation());
        schedule.setParticipants(request.getParticipants());

        if(request.isExistTag()){
            schedule.setTag(tagRepository.getReferenceById(request.getTagId()));
        } else{
            schedule.setColor(request.getColor());
            schedule.setIcon(iconRepository.getReferenceById(request.getIconId()));
        }

        return ScheduleResponse.from(schedule);
    }

    @Transactional
    public void deleteSchedule(String userId, Long scheduleId) {
        Schedule schedule = getById(scheduleId);

        if (!schedule.getUser().getId().equals(UUID.fromString(userId))) {
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        scheduleRepository.delete(schedule);
    }
}
