package com.fluento.features.progress.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.progress.enums.AchievementType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "achievements")
public class Achievement extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, unique = true, length = 50)
    private AchievementType type;

    @Column(name = "title", nullable = false, length = 180)
    private String title;

    @Column(name = "description", nullable = false, length = 500)
    private String description;

    @Column(name = "xp_reward", nullable = false)
    private int xpReward;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;
}
