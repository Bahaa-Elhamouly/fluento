package com.fluento.features.user.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.course.enums.LearningLevel;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_profiles")
public class UserProfile extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @Column(name = "native_language_code", length = 10)
    private String nativeLanguageCode;

    @Column(name = "target_language_codes", nullable = false, length = 200)
    private String targetLanguageCodes = "";

    @Enumerated(EnumType.STRING)
    @Column(name = "learning_level", length = 40)
    private LearningLevel learningLevel;

    @Column(name = "daily_goal_minutes")
    private Integer dailyGoalMinutes;
}
