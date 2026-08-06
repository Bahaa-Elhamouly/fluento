package com.fluento.features.grammar.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.LearningLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "grammar_rules")
public class GrammarRule extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "title", nullable = false, length = 180)
    private String title;

    @Column(name = "slug", nullable = false, unique = true, length = 200)
    private String slug;

    @Column(name = "explanation", nullable = false, length = 3000)
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 40)
    private LearningLevel level;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "position", nullable = false)
    private int position;
}
