package com.fluento.features.user.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.enums.LearningLevel;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.user.dto.UpdateUserProfileRequest;
import com.fluento.features.user.dto.UserProfileResponse;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.entity.UserProfile;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserProfileRepository;
import com.fluento.features.user.repository.UserRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    @Test
    void getProfileReturnsUserWithEmptyPreferencesWhenProfileIsMissing() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.empty());

        UserProfileResponse response =
                userProfileService.getProfile(user.getId());

        assertThat(response.id()).isEqualTo(user.getId());
        assertThat(response.email()).isEqualTo("sara@example.com");
        assertThat(response.targetLanguageCodes()).isEmpty();
        assertThat(response.learningLevel()).isNull();
    }

    @Test
    void updateProfileCreatesProfileAndNormalizesLanguages() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.empty());
        when(languageRepository.findByCodeAndDeletedAtIsNull("de"))
                .thenReturn(Optional.of(language("de")));
        when(languageRepository.findByCodeAndDeletedAtIsNull("es"))
                .thenReturn(Optional.of(language("es")));
        when(userProfileRepository.save(any(UserProfile.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        UserProfileResponse response = userProfileService.updateProfile(
                user.getId(),
                new UpdateUserProfileRequest(
                        "https://example.com/avatar.png",
                        " AR ",
                        List.of("DE", "es", "de"),
                        LearningLevel.BEGINNER,
                        20
                )
        );

        assertThat(response.avatarUrl())
                .isEqualTo("https://example.com/avatar.png");
        assertThat(response.nativeLanguageCode()).isEqualTo("ar");
        assertThat(response.targetLanguageCodes())
                .containsExactly("de", "es");
        assertThat(response.learningLevel())
                .isEqualTo(LearningLevel.BEGINNER);
        assertThat(response.dailyGoalMinutes()).isEqualTo(20);
    }

    @Test
    void updateProfileThrowsWhenLanguageDoesNotExist() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUser_IdAndDeletedAtIsNull(user.getId()))
                .thenReturn(Optional.empty());
        when(languageRepository.findByCodeAndDeletedAtIsNull("it"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> userProfileService.updateProfile(
                        user.getId(),
                        new UpdateUserProfileRequest(
                                null,
                                null,
                                List.of("it"),
                                null,
                                null
                        )
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getProfileThrowsWhenUserIsMissing() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProfileService.getProfile(userId))
                .isInstanceOf(ResourceNotFoundException.class);
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

    private Language language(String code) {
        Language language = new Language();
        language.setId(UUID.randomUUID());
        language.setCode(code);
        language.setName(code);
        language.setNativeName(code);
        language.setEnabled(true);
        return language;
    }
}
