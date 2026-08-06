package com.fluento.features.vocabulary.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(
        name = "user_vocabulary",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_vocabulary_user_term",
                columnNames = {"user_id", "term_id"}
        )
)
public class UserVocabulary extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "term_id", nullable = false)
    private VocabularyTerm term;

    @Column(name = "favorite", nullable = false)
    private boolean favorite;

    @Column(name = "mastery_level", nullable = false)
    private int masteryLevel;

    @Column(name = "last_reviewed_at")
    private LocalDateTime lastReviewedAt;
}
