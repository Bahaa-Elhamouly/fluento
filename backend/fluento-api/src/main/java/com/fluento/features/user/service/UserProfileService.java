package com.fluento.features.user.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.user.dto.UpdateUserProfileRequest;
import com.fluento.features.user.dto.UserProfileResponse;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.entity.UserProfile;
import com.fluento.features.user.repository.UserProfileRepository;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final LanguageRepository languageRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse getProfile(UUID userId) {
        User user = getUser(userId);
        UserProfile profile = userProfileRepository
                .findByUser_IdAndDeletedAtIsNull(userId)
                .orElse(null);

        return toResponse(user, profile);
    }

    @Transactional
    public UserProfileResponse updateProfile(
            UUID userId,
            UpdateUserProfileRequest request
    ) {
        User user = getUser(userId);
        UserProfile profile = userProfileRepository
                .findByUser_IdAndDeletedAtIsNull(userId)
                .orElseGet(() -> createProfile(user));

        if (request.avatarUrl() != null) {
            profile.setAvatarUrl(request.avatarUrl().trim());
        }
        if (request.nativeLanguageCode() != null) {
            profile.setNativeLanguageCode(
                    normalizeOptionalLanguageCode(request.nativeLanguageCode())
            );
        }
        if (request.targetLanguageCodes() != null) {
            profile.setTargetLanguageCodes(
                    toLanguageCodeCsv(request.targetLanguageCodes())
            );
        }
        if (request.learningLevel() != null) {
            profile.setLearningLevel(request.learningLevel());
        }
        if (request.dailyGoalMinutes() != null) {
            profile.setDailyGoalMinutes(request.dailyGoalMinutes());
        }

        UserProfile savedProfile = userProfileRepository.save(profile);

        return toResponse(user, savedProfile);
    }

    private User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
    }

    private UserProfile createProfile(User user) {
        UserProfile profile = new UserProfile();
        profile.setUser(user);
        profile.setTargetLanguageCodes("");
        return profile;
    }

    private String toLanguageCodeCsv(List<String> languageCodes) {
        LinkedHashSet<String> normalizedCodes = new LinkedHashSet<>();
        for (String languageCode : languageCodes) {
            normalizedCodes.add(normalizeLanguageCode(languageCode));
        }

        return String.join(",", normalizedCodes);
    }

    private String normalizeLanguageCode(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            throw new ResourceNotFoundException("Language", "blank");
        }

        String normalizedLanguage = languageCode.trim().toLowerCase();
        if (languageRepository
                .findByCodeAndDeletedAtIsNull(normalizedLanguage)
                .isEmpty()) {
            throw new ResourceNotFoundException(
                    "Language",
                    normalizedLanguage
            );
        }

        return normalizedLanguage;
    }

    private String normalizeOptionalLanguageCode(String languageCode) {
        if (languageCode == null || languageCode.isBlank()) {
            return null;
        }

        return languageCode.trim().toLowerCase();
    }

    private UserProfileResponse toResponse(
            User user,
            UserProfile profile
    ) {
        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.isEmailVerified(),
                profile == null ? null : profile.getAvatarUrl(),
                profile == null ? null : profile.getNativeLanguageCode(),
                profile == null
                        ? List.of()
                        : toLanguageCodeList(profile.getTargetLanguageCodes()),
                profile == null ? null : profile.getLearningLevel(),
                profile == null ? null : profile.getDailyGoalMinutes()
        );
    }

    private List<String> toLanguageCodeList(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return List.of(value.split(","));
    }
}
