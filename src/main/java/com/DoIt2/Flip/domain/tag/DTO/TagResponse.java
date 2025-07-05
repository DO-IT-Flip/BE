package com.DoIt2.Flip.domain.tag.DTO;

import com.DoIt2.Flip.domain.tag.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@Builder
@AllArgsConstructor
public class TagResponse {
    private Long tagId;
    private String name;
    private String color;
    private Long iconId;
    private ZonedDateTime createdAt;
    private ZonedDateTime updatedAt;

    public static TagResponse from(Tag tag){
        return TagResponse.builder()
                .tagId(tag.getTagId())
                .name(tag.getName())
                .color(tag.getColor())
                .iconId(tag.getIcon().getIconId())
                .createdAt(tag.getCreatedAt())
                .updatedAt(tag.getUpdatedAt())
                .build();
    }
}
