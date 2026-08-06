package com.fluento.features.vocabulary.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Language;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import com.fluento.features.vocabulary.dto.UpdateUserVocabularyRequest;
import com.fluento.features.vocabulary.dto.UserVocabularyResponse;
import com.fluento.features.vocabulary.entity.UserVocabulary;
import com.fluento.features.vocabulary.entity.VocabularyTerm;
import com.fluento.features.vocabulary.repository.UserVocabularyRepository;
import com.fluento.features.vocabulary.repository.VocabularyTermRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserVocabularyServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private VocabularyTermRepository vocabularyTermRepository;

    @Mock
    private UserVocabularyRepository userVocabularyRepository;

    @InjectMocks
    private UserVocabularyService userVocabularyService;

    @Test
    void getVocabularyReturnsUserVocabulary() {
        User user = user();
        UserVocabulary vocabulary = vocabulary(user, term());
        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(userVocabularyRepository
                .findByUser_IdAndDeletedAtIsNullOrderByUpdatedAtDesc(
                        user.getId()
                ))
                .thenReturn(List.of(vocabulary));

        List<UserVocabularyResponse> response =
                userVocabularyService.getVocabulary(user.getId(), false);

        assertThat(response).hasSize(1);
        assertThat(response.getFirst().term()).isEqualTo("Hallo");
    }

    @Test
    void updateVocabularyCreatesStateForTerm() {
        User user = user();
        VocabularyTerm term = term();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(vocabularyTermRepository
                .findByIdAndEnabledTrueAndDeletedAtIsNull(term.getId()))
                .thenReturn(Optional.of(term));
        when(userVocabularyRepository
                .findByUser_IdAndTerm_IdAndDeletedAtIsNull(
                        user.getId(),
                        term.getId()
                ))
                .thenReturn(Optional.empty());
        when(userVocabularyRepository.save(org.mockito.ArgumentMatchers.any(
                UserVocabulary.class
        )))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserVocabularyResponse response =
                userVocabularyService.updateVocabulary(
                        user.getId(),
                        term.getId(),
                        new UpdateUserVocabularyRequest(true, 3, true)
                );

        assertThat(response.favorite()).isTrue();
        assertThat(response.masteryLevel()).isEqualTo(3);
        assertThat(response.lastReviewedAt()).isNotNull();
    }

    @Test
    void updateVocabularyThrowsWhenTermIsMissing() {
        User user = user();
        UUID termId = UUID.randomUUID();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(vocabularyTermRepository
                .findByIdAndEnabledTrueAndDeletedAtIsNull(termId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> userVocabularyService.updateVocabulary(
                        user.getId(),
                        termId,
                        new UpdateUserVocabularyRequest(true, null, null)
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private UserVocabulary vocabulary(User user, VocabularyTerm term) {
        UserVocabulary vocabulary = new UserVocabulary();
        vocabulary.setId(UUID.randomUUID());
        vocabulary.setUser(user);
        vocabulary.setTerm(term);
        vocabulary.setFavorite(true);
        vocabulary.setMasteryLevel(2);
        return vocabulary;
    }

    private VocabularyTerm term() {
        VocabularyTerm term = new VocabularyTerm();
        term.setId(UUID.randomUUID());
        term.setLanguage(language());
        term.setTerm("Hallo");
        term.setTranslation("Hello");
        term.setPronunciation("HAH-loh");
        term.setExample("Hallo, Sara.");
        term.setTags("greetings");
        term.setEnabled(true);
        return term;
    }

    private Language language() {
        Language language = new Language();
        language.setId(UUID.randomUUID());
        language.setCode("de");
        language.setName("German");
        language.setNativeName("Deutsch");
        language.setEnabled(true);
        return language;
    }

    private User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("Sara");
        user.setLastName("Ahmed");
        user.setEmail("sara@example.com");
        user.setPasswordHash("hash");
        user.setRole(Role.USER);
        user.setEnabled(true);
        return user;
    }
}
