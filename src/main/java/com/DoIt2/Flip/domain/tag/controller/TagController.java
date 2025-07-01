package com.DoIt2.Flip.domain.tag.controller;

import com.DoIt2.Flip.domain.tag.DTO.TagRequest;
import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import com.DoIt2.Flip.domain.tag.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @PostMapping
    public ResponseEntity<TagResponse> createTag(@RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.createTag(request));
    }

    @GetMapping
    public ResponseEntity<List<TagResponse>> getAllTags() {
        return ResponseEntity.ok(tagService.getAllTags());
    }

    @PutMapping("/{tagId}")
    public ResponseEntity<TagResponse> updateTag(@PathVariable Long tagId,
                                                 @RequestBody TagRequest request) {
        return ResponseEntity.ok(tagService.updateTag(tagId, request));
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.noContent().build();
    }
}
