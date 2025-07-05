package com.DoIt2.Flip.domain.tag.controller;

import com.DoIt2.Flip.domain.auth.dto.CustomUserDetails;
import com.DoIt2.Flip.domain.tag.DTO.TagRequest;
import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import com.DoIt2.Flip.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.createTag(userDetails.getUserId(), request));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(tagService.getAllTags(userDetails.getUserId()));
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponse> updateTag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                 @PathVariable Long tagId,
                                                 @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.updateTag(userDetails.getUserId(), tagId, request));
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @PathVariable Long tagId) {
        tagService.deleteTag(userDetails.getUserId(), tagId);
        return ResponseEntity.noContent().build();
    }
}
