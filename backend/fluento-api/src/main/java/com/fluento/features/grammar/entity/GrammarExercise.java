package com.fluento.features.grammar.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.grammar.enums.GrammarExerciseType;
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
@Table(name = "grammar_exercises")
public class GrammarExercise extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_id", nullable = false)
    private GrammarRule rule;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 40)
    private GrammarExerciseType type;

    @Column(name = "prompt", nullable = false, length = 1000)
    private String prompt;

    @Column(name = "options_json", length = 2000)
    private String optionsJson;

    @Column(name = "correct_answer", nullable = false, length = 500)
    private String correctAnswer;

    @Column(name = "explanation", length = 1000)
    private String explanation;

    @Enumerated(EnumType.STRING)
    @Column(name = "level", nullable = false, length = 40)
    private LearningLevel level;

    @Column(name = "points", nullable = false)
    private int points = 1;

    @Column(name = "position", nullable = false)
    private int position;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
}
