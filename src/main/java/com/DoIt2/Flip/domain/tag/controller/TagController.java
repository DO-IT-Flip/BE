package com.DoIt2.Flip.domain.tag.controller;

import com.DoIt2.Flip.domain.auth.dto.CustomUserDetails;
import com.DoIt2.Flip.domain.tag.DTO.TagRequest;
import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import com.DoIt2.Flip.domain.tag.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Tag API", description = "태그 관련 API")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "태그 생성")
    @PostMapping
    public ResponseEntity<TagResponse> createTag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.createTag(userDetails.getUserId(), request));
    }

    @Operation(summary = "태그 전체 조회")
    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.getAllTags(userDetails.getUserId()));
    }

    @Operation(summary = "태그 상세 조회")
    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponse> updateTag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long tagId,
                                                 @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.updateTag(userDetails.getUserId(), tagId, request));
    }

    @Operation(summary = "태그 삭제")
    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable Long tagId) {
        tagService.deleteTag(userDetails.getUserId(), tagId);
        return ResponseEntity.noContent().build();
    }
}
