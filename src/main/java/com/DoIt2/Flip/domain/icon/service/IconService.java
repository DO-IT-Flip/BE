package com.DoIt2.Flip.domain.icon.service;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import com.DoIt2.Flip.domain.icon.repository.IconRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IconService {

    private final IconRepository iconRepository;

    public List<Icon> getAllIcons() {
        return iconRepository.findAll();
    }

    public Icon getIconById(Long id) {
        return iconRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("아이콘을 찾을 수 없습니다."));
    }

    public Icon saveIcon(Icon icon) {
        return iconRepository.save(icon);
    }

    public void deleteIcon(Long id) {
        iconRepository.deleteById(id);
    }
}
