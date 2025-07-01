package com.DoIt2.Flip.domain.tag.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TagResponse {
    private Long tagId;
    private String name;
    private String color;
    private Long iconId;
}
