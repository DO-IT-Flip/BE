package com.DoIt2.Flip.domain.quote.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "quotes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quote_id")
    private Long id;

    @Column(name = "kr_content", nullable = false, columnDefinition = "TEXT")
    private String krContent;

    @Column(name = "en_content", nullable = false, columnDefinition = "TEXT")
    private String enContent;
}
