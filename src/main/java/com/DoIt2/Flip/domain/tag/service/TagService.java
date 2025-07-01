package com.DoIt2.Flip.domain.tag.service;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import com.DoIt2.Flip.domain.icon.repository.IconRepository;
import com.DoIt2.Flip.domain.tag.DTO.TagRequest;
import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import com.DoIt2.Flip.domain.tag.entity.Tag;
import com.DoIt2.Flip.domain.tag.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final IconRepository iconRepository;

    public TagResponse createTag(TagRequest request) {
        Icon icon = iconRepository.findById(request.getIconId()).orElse(null);
        Tag tag = new Tag(request.getName(), request.getColor(), icon);
        Tag saved = tagRepository.save(tag);
        return new TagResponse(saved.getTagId(), saved.getName(), saved.getColor(),
                icon != null ? icon.getIconId() : null);
    }

    public List<TagResponse> getAllTags() {
        return tagRepository.findAll().stream()
                .map(tag -> new TagResponse(
                        tag.getTagId(), tag.getName(), tag.getColor(),
                        tag.getIcon() != null ? tag.getIcon().getIconId() : null
                )).collect(Collectors.toList());
    }

    public TagResponse updateTag(Long tagId, TagRequest request) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("해당 태그가 존재하지 않습니다."));
        Icon icon = iconRepository.findById(request.getIconId()).orElse(null);
        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tag.setIcon(icon);
        return new TagResponse(tag.getTagId(), tag.getName(), tag.getColor(),
                icon != null ? icon.getIconId() : null);
    }

    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }
}
