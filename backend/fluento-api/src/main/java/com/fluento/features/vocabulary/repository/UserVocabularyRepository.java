package com.fluento.features.vocabulary.repository;

import com.fluento.features.vocabulary.entity.UserVocabulary;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserVocabularyRepository
        extends JpaRepository<UserVocabulary, UUID> {

    @EntityGraph(attributePaths = {"term", "term.language"})
    List<UserVocabulary> findByUser_IdAndDeletedAtIsNullOrderByUpdatedAtDesc(
            UUID userId
    );

    @EntityGraph(attributePaths = {"term", "term.language"})
    List<UserVocabulary> findByUser_IdAndFavoriteTrueAndDeletedAtIsNullOrderByUpdatedAtDesc(
            UUID userId
    );

    Optional<UserVocabulary> findByUser_IdAndTerm_IdAndDeletedAtIsNull(
            UUID userId,
            UUID termId
    );
}
