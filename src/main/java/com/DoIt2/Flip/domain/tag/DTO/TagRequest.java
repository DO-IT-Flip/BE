package com.DoIt2.Flip.domain.tag.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    private String name;
    private String color;
    private Long iconId;
}
