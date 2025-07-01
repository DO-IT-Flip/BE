package com.DoIt2.Flip.domain.tag.entity;

import com.DoIt2.Flip.domain.icon.entity.Icon;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "icon_id")
    private Icon icon;

    // 이름만 받는 생성자
    public Tag(String name) {
        this.name = name;
        this.color = "gray";
        this.icon = null;
    }

    // name, color, icon 모두 받는 생성자 (TagService에서 사용)
    public Tag(String name, String color, Icon icon) {
        this.name = name;
        this.color = color;
        this.icon = icon;
    }
}
