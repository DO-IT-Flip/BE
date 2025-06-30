package com.DoIt2.Flip.domain.tag.DTO;

public class TagResponse {
    private Long tagId;
    private String name;
    private String color;
    private Long iconId;

    public TagResponse(Long tagId, String name, String color, Long iconId) {
        this.tagId = tagId;
        this.name = name;
        this.color = color;
        this.iconId = iconId;
    }

    public Long getTagId() { return tagId; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public Long getIconId() { return iconId; }
}
