package com.fluento.features.quiz.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.quiz.enums.QuizStatus;
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
@Table(name = "quizzes")
public class Quiz extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "lesson_id", nullable = false)
    private Lesson lesson;

    @Column(name = "title", nullable = false, length = 180)
    private String title;

    @Column(name = "description", length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private QuizStatus status = QuizStatus.DRAFT;

    @Column(name = "passing_score", nullable = false)
    private int passingScore;

    @Column(name = "position", nullable = false)
    private int position;
}
