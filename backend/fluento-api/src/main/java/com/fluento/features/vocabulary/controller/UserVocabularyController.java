package com.fluento.features.vocabulary.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.vocabulary.dto.UpdateUserVocabularyRequest;
import com.fluento.features.vocabulary.dto.UserVocabularyResponse;
import com.fluento.features.vocabulary.service.UserVocabularyService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vocabulary")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class UserVocabularyController {

    private final UserVocabularyService userVocabularyService;

    @GetMapping
    public ApiResponse<List<UserVocabularyResponse>> vocabulary(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @RequestParam(defaultValue = "false") boolean favoritesOnly
    ) {
        return ApiResponse.success(
                "User vocabulary retrieved successfully",
                userVocabularyService.getVocabulary(
                        userDetails.getId(),
                        favoritesOnly
                )
        );
    }

    @PatchMapping("/{termId}")
    public ApiResponse<UserVocabularyResponse> updateVocabulary(
            @AuthenticationPrincipal FluentoUserDetails userDetails,
            @PathVariable UUID termId,
            @Valid @RequestBody UpdateUserVocabularyRequest request
    ) {
        return ApiResponse.success(
                "User vocabulary updated successfully",
                userVocabularyService.updateVocabulary(
                        userDetails.getId(),
                        termId,
                        request
                )
        );
    }
}
