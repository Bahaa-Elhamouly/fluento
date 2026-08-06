package com.fluento.features.progress.entity;

import com.fluento.common.entity.BaseEntity;
import com.fluento.features.progress.enums.XpEventType;
import com.fluento.features.user.entity.User;
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

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "xp_events")
public class XpEvent extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 40)
    private XpEventType eventType;

    @Column(name = "source_id")
    private UUID sourceId;

    @Column(name = "xp", nullable = false)
    private int xp;

    @Column(name = "description", nullable = false, length = 300)
    private String description;
}
