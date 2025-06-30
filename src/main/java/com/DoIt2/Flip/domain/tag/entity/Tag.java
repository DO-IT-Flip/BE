package com.DoIt2.Flip.domain.tag.entity;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import jakarta.persistence.*;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String name;

    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon;

    public Tag() {}

    // ✅ name만 받는 생성자 추가
    public Tag(String name) {
        this.name = name;
        this.color = "gray"; // 기본 색상
        this.icon = null;    // 기본 아이콘 없음
    }

    public Tag(String name, String color, Icon icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }

    public Long getTagId() { return tagId; }
    public String getName() { return name; }
    public String getColor() { return color; }
    public Icon getIcon() { return icon; }

    public void setName(String name) { this.name = name; }
    public void setColor(String color) { this.color = color; }
    public void setIcon(Icon icon) { this.icon = icon; }
}
