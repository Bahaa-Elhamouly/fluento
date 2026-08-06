package com.fluento.features.grammar.entity;

import com.fluento.common.entity.BaseEntity;
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
@Table(name = "grammar_practice_attempt_answers")
public class GrammarPracticeAttemptAnswer extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "attempt_id", nullable = false)
    private GrammarPracticeAttempt attempt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "exercise_id", nullable = false)
    private GrammarExercise exercise;

    @Column(name = "submitted_answer", nullable = false, length = 500)
    private String submittedAnswer;

    @Column(name = "correct", nullable = false)
    private boolean correct;

    @Column(name = "points_awarded", nullable = false)
    private int pointsAwarded;
}
