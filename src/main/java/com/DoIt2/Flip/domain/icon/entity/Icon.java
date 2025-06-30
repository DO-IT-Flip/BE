package com.DoIt2.Flip.domain.icon.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Icon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iconId;

    private String iconName;
    private String color;

    public Icon(String iconName, String color) {
        this.iconName = iconName;
        this.color = color;
    }
}
