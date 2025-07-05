package com.DoIt2.Flip.domain.icon.controller;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import com.DoIt2.Flip.domain.icon.service.IconService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/icons")
@RequiredArgsConstructor
public class IconController {

    private final IconService iconService;

    // 모든 아이콘 조회
    @GetMapping
    public ResponseEntity<List<Icon>> getAllIcons() {
        List<Icon> icons = iconService.getAllIcons();
        return ResponseEntity.ok(icons);
    }

    // ID로 아이콘 조회
    @GetMapping("/{id}")
    public ResponseEntity<Icon> getIconById(@PathVariable Long id) {
        Icon icon = iconService.getIconById(id);
        return ResponseEntity.ok(icon);
    }

    // 아이콘 생성
    @PostMapping
    public ResponseEntity<Icon> createIcon(@RequestBody Icon icon) {
        Icon savedIcon = iconService.saveIcon(icon);
        return ResponseEntity.ok(savedIcon);
    }

    // 아이콘 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIcon(@PathVariable Long id) {
        iconService.deleteIcon(id);
        return ResponseEntity.noContent().build();
    }
}
