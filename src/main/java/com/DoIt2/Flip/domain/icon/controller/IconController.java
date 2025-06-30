package com.DoIt2.Flip.domain.icon.controller;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import com.DoIt2.Flip.domain.icon.service.IconService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/icons")
@RequiredArgsConstructor
public class IconController {

    private final IconService iconService;

    @GetMapping
    public List<Icon> getAllIcons() {
        return iconService.getAllIcons();
    }

    @GetMapping("/{id}")
    public Icon getIconById(@PathVariable Long id) {
        return iconService.getIconById(id);
    }

    @PostMapping
    public Icon createIcon(@RequestBody Icon icon) {
        return iconService.saveIcon(icon);
    }

    @DeleteMapping("/{id}")
    public void deleteIcon(@PathVariable Long id) {
        iconService.deleteIcon(id);
    }
}
