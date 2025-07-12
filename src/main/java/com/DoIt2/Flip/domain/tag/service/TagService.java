package com.DoIt2.Flip.domain.tag.service;

import com.DoIt2.Flip.domain.icon.repository.IconRepository;
import com.DoIt2.Flip.domain.tag.DTO.TagRequest;
import com.DoIt2.Flip.domain.tag.DTO.TagResponse;
import com.DoIt2.Flip.domain.tag.entity.Tag;
import com.DoIt2.Flip.domain.tag.repository.TagRepository;
import com.DoIt2.Flip.domain.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;
    private final IconRepository iconRepository;

    public Tag getById(Long tagId){
        return tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("해당 태그가 존재하지 않습니다."));
    }

    @Transactional
    public TagResponse createTag(String userId, TagRequest request) {

        Tag tag = Tag.builder()
                .user(userRepository.getReferenceById(UUID.fromString(userId))) // 불필요한 DB 쿼리 방지
                .name(request.getName())
                .color(request.getColor())
                .icon(iconRepository.getReferenceById(request.getIconId()))
                .build();

        Tag saved = tagRepository.save(tag);
        return TagResponse.from(saved);
    }

    public List<TagResponse> getAllTags(String userId) {
        return tagRepository.findByUserId(UUID.fromString(userId)).stream()
                .map(TagResponse::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public TagResponse updateTag(String userId, Long tagId, TagRequest request) {
        Tag tag = getById(tagId);

        if(!tag.getUser().getId().equals(UUID.fromString(userId))){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        tag.setName(request.getName());
        tag.setColor(request.getColor());
        tag.setIcon(iconRepository.getReferenceById(request.getIconId()));
        return TagResponse.from(tag);
    }

    @Transactional
    public void deleteTag(String userId, Long tagId) {
        Tag tag = getById(tagId);
        if(!tag.getUser().getId().equals(UUID.fromString(userId))){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }

        tagRepository.delete(tag);
    }
}
