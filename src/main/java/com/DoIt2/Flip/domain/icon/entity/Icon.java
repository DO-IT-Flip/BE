package com.DoIt2.Flip.domain.icon.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "icons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Icon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iconId;

    @Column(nullable = false)
    private String iconName;

    @Column(nullable = false)
    private String color;
}
