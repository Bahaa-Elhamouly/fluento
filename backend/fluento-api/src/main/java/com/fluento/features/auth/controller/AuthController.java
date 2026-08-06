package com.fluento.features.auth.controller;

import com.fluento.common.response.ApiResponse;
import com.fluento.features.auth.dto.request.EmailRequest;
import com.fluento.features.auth.dto.request.LoginRequest;
import com.fluento.features.auth.dto.request.RefreshTokenRequest;
import com.fluento.features.auth.dto.request.RegisterRequest;
import com.fluento.features.auth.dto.request.ResetPasswordRequest;
import com.fluento.features.auth.dto.request.TokenRequest;
import com.fluento.features.auth.dto.response.AuthResponse;
import com.fluento.features.auth.dto.response.TokenIssuedResponse;
import com.fluento.features.auth.service.AuthRecoveryService;
import com.fluento.features.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthRecoveryService authRecoveryService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        AuthResponse response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Account created successfully",
                                response
                        )
                );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        AuthResponse response = authService.login(request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Login successful",
                        response
                )
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        AuthResponse response = authService.refresh(
                request.refreshToken()
        );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Token refreshed successfully",
                        response
                )
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @Valid @RequestBody RefreshTokenRequest request
    ) {
        authService.logout(request.refreshToken());

        return ResponseEntity.ok(
                ApiResponse.success("Logout successful")
        );
    }

    @PostMapping("/email-verification/request")
    public ResponseEntity<ApiResponse<TokenIssuedResponse>>
    requestEmailVerification(
            @Valid @RequestBody EmailRequest request
    ) {
        TokenIssuedResponse response =
                authRecoveryService.issueEmailVerificationToken(
                        request.email()
                );

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Email verification token issued successfully",
                        response
                )
        );
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(
            @Valid @RequestBody TokenRequest request
    ) {
        authRecoveryService.verifyEmail(request.token());

        return ResponseEntity.ok(
                ApiResponse.success("Email verified successfully")
        );
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<TokenIssuedResponse>> forgotPassword(
            @Valid @RequestBody EmailRequest request
    ) {
        TokenIssuedResponse response =
                authRecoveryService.issuePasswordResetToken(request.email());

        return ResponseEntity.ok(
                ApiResponse.success(
                        "If the email exists, a password reset token was issued",
                        response
                )
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Void>> resetPassword(
            @Valid @RequestBody ResetPasswordRequest request
    ) {
        authRecoveryService.resetPassword(
                request.token(),
                request.newPassword()
        );

        return ResponseEntity.ok(
                ApiResponse.success("Password reset successfully")
        );
    }
}
