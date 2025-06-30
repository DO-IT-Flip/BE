package com.DoIt2.Flip.domain.tag.DTO;

public class TagRequest {
    private String name;
    private String color;
    private Long iconId;

    public TagRequest() {}

    public TagRequest(String name, String color, Long iconId) {
        this.name = name;
        this.color = color;
        this.iconId = iconId;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Long getIconId() { return iconId; }
    public void setIconId(Long iconId) { this.iconId = iconId; }
}
