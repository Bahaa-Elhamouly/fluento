package com.fluento.features.vocabulary.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import com.fluento.features.vocabulary.dto.UpdateUserVocabularyRequest;
import com.fluento.features.vocabulary.dto.UserVocabularyResponse;
import com.fluento.features.vocabulary.entity.UserVocabulary;
import com.fluento.features.vocabulary.entity.VocabularyTerm;
import com.fluento.features.vocabulary.repository.UserVocabularyRepository;
import com.fluento.features.vocabulary.repository.VocabularyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserVocabularyService {

    private final UserRepository userRepository;
    private final VocabularyTermRepository vocabularyTermRepository;
    private final UserVocabularyRepository userVocabularyRepository;

    @Transactional(readOnly = true)
    public List<UserVocabularyResponse> getVocabulary(
            UUID userId,
            boolean favoritesOnly
    ) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        List<UserVocabulary> vocabulary = favoritesOnly
                ? userVocabularyRepository
                .findByUser_IdAndFavoriteTrueAndDeletedAtIsNullOrderByUpdatedAtDesc(
                        userId
                )
                : userVocabularyRepository
                .findByUser_IdAndDeletedAtIsNullOrderByUpdatedAtDesc(userId);

        return vocabulary.stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public UserVocabularyResponse updateVocabulary(
            UUID userId,
            UUID termId,
            UpdateUserVocabularyRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        VocabularyTerm term = vocabularyTermRepository
                .findByIdAndEnabledTrueAndDeletedAtIsNull(termId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Vocabulary term",
                                termId
                        )
                );

        UserVocabulary vocabulary = userVocabularyRepository
                .findByUser_IdAndTerm_IdAndDeletedAtIsNull(userId, termId)
                .orElseGet(() -> createVocabulary(user, term));

        if (request.favorite() != null) {
            vocabulary.setFavorite(request.favorite());
        }
        if (request.masteryLevel() != null) {
            vocabulary.setMasteryLevel(request.masteryLevel());
        }
        if (Boolean.TRUE.equals(request.markReviewed())) {
            vocabulary.setLastReviewedAt(LocalDateTime.now());
        }

        return toResponse(userVocabularyRepository.save(vocabulary));
    }

    private UserVocabulary createVocabulary(User user, VocabularyTerm term) {
        UserVocabulary vocabulary = new UserVocabulary();
        vocabulary.setUser(user);
        vocabulary.setTerm(term);
        vocabulary.setFavorite(false);
        vocabulary.setMasteryLevel(0);
        return vocabulary;
    }

    private UserVocabularyResponse toResponse(UserVocabulary vocabulary) {
        VocabularyTerm term = vocabulary.getTerm();

        return new UserVocabularyResponse(
                term.getId(),
                term.getLanguage().getCode(),
                term.getTerm(),
                term.getTranslation(),
                vocabulary.isFavorite(),
                vocabulary.getMasteryLevel(),
                vocabulary.getLastReviewedAt()
        );
    }
}
