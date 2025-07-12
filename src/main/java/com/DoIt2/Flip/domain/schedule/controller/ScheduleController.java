package com.DoIt2.Flip.domain.schedule.controller;

import com.DoIt2.Flip.domain.schedule.dto.ScheduleRequest;
import com.DoIt2.Flip.domain.schedule.dto.ScheduleResponse;
import com.DoIt2.Flip.domain.schedule.service.ScheduleService;
import com.DoIt2.Flip.domain.auth.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Schedule API", description = "일정 관련 API")
@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    // 스케줄 생성
    @Operation(summary = "일정 생성")
    @PostMapping
    public ResponseEntity<ScheduleResponse> createSchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody ScheduleRequest request
    ) {
        String userId = userDetails.getUserId(); // 토큰에서 userId 추출
        ScheduleResponse response = scheduleService.createSchedule(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 스케줄 전체 조회 (태그 포함)
    // 특정 날짜(year, month, day)로 필터링
    @Operation(summary = "일정 조회", description = "특정 날짜(year, month, day)로 필터링")
    @GetMapping
    public ResponseEntity<List<ScheduleResponse>> getAllSchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer day
    ) {
        String userId = userDetails.getUserId();

        if (year != null && month != null && day != null) {
            List<ScheduleResponse> filtered = scheduleService.getSchedulesByDate(userId, year, month, day);
            return ResponseEntity.ok(filtered);
        }

        List<ScheduleResponse> response = scheduleService.getAllSchedules(userId);
        return ResponseEntity.ok(response);
    }

    // 키워드로 일정 검색
    @Operation(summary = "일정 검색", description = "특정 키워드로 일정 검색")
    @GetMapping("/search")
    public ResponseEntity<List<ScheduleResponse>> searchSchedules(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam String keyword
    ) {
        String userId = userDetails.getUserId();
        List<ScheduleResponse> response = scheduleService.searchSchedules(userId, keyword);
        return ResponseEntity.ok(response);
    }

    // 스케줄 수정
    @Operation(summary = "일정 수정")
    @PatchMapping("/{scheduleId}")
    public ResponseEntity<ScheduleResponse> updateSchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long scheduleId,
            @RequestBody ScheduleRequest request
    ) {
        String userId = userDetails.getUserId();
        ScheduleResponse response = scheduleService.updateSchedule(userId, scheduleId, request);
        return ResponseEntity.ok(response);
    }

    // 스케줄 삭제
    @Operation(summary = "일정 삭제")
    @DeleteMapping("/{scheduleId}")
    public ResponseEntity<Void> deleteSchedule(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long scheduleId
    ) {
        String userId = userDetails.getUserId();
        scheduleService.deleteSchedule(userId, scheduleId);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
