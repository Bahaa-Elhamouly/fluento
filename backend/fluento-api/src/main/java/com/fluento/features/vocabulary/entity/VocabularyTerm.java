package com.fluento.features.vocabulary.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.course.entity.Language;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "vocabulary_terms")
public class VocabularyTerm extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "language_id", nullable = false)
    private Language language;

    @Column(name = "term", nullable = false, length = 160)
    private String term;

    @Column(name = "translation", nullable = false, length = 160)
    private String translation;

    @Column(name = "pronunciation", length = 160)
    private String pronunciation;

    @Column(name = "example", length = 1000)
    private String example;

    @Column(name = "tags", nullable = false, length = 500)
    private String tags;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
}
