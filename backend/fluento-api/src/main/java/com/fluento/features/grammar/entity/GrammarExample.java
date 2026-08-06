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
@Table(name = "grammar_examples")
public class GrammarExample extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "rule_id", nullable = false)
    private GrammarRule rule;

    @Column(name = "source_text", nullable = false, length = 500)
    private String sourceText;

    @Column(name = "translation", nullable = false, length = 500)
    private String translation;

    @Column(name = "note", length = 500)
    private String note;

    @Column(name = "position", nullable = false)
    private int position;
}
