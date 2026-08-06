package com.fluento.features.progress.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_streaks")
public class UserStreak extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "current_streak_days", nullable = false)
    private int currentStreakDays;

    @Column(name = "longest_streak_days", nullable = false)
    private int longestStreakDays;

    @Column(name = "last_activity_date")
    private LocalDate lastActivityDate;
}
