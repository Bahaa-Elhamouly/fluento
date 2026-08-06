package com.fluento.features.auth.service;

import com.fluento.common.enums.ErrorCode;
import com.fluento.common.exception.BusinessException;
import com.fluento.features.auth.dto.request.LoginRequest;
import com.fluento.features.auth.dto.request.RegisterRequest;
import com.fluento.features.auth.dto.response.AuthResponse;
import com.fluento.features.auth.dto.response.UserSummaryResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.auth.security.JwtService;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        if (userRepository.existsByEmailIgnoreCaseAndDeletedAtIsNull(
                normalizedEmail
        )) {
            throw new BusinessException(
                    ErrorCode.EMAIL_ALREADY_EXISTS
            );
        }

        User user = new User();

        user.setFirstName(request.firstName().trim());
        user.setLastName(request.lastName().trim());
        user.setEmail(normalizedEmail);
        user.setPasswordHash(
                passwordEncoder.encode(request.password())
        );
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setEmailVerified(false);

        User savedUser = userRepository.save(user);

        FluentoUserDetails userDetails =
                new FluentoUserDetails(savedUser);

        String accessToken = jwtService.generateToken(userDetails);
        RefreshTokenResult refreshToken =
                refreshTokenService.create(savedUser);

        return buildAuthResponse(
                savedUser,
                accessToken,
                refreshToken.refreshToken()
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = normalizeEmail(request.email());

        try {
            Authentication authentication =
                    authenticationManager.authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    normalizedEmail,
                                    request.password()
                            )
                    );

            FluentoUserDetails userDetails =
                    (FluentoUserDetails) authentication.getPrincipal();

            User user = userRepository
                    .findByEmailIgnoreCaseAndDeletedAtIsNull(
                            userDetails.getUsername()
                    )
                    .orElseThrow(
                            () -> new BadCredentialsException(
                                    "Invalid email or password"
                            )
                    );

            String accessToken =
                    jwtService.generateToken(userDetails);
            RefreshTokenResult refreshToken =
                    refreshTokenService.create(user);

            return buildAuthResponse(
                    user,
                    accessToken,
                    refreshToken.refreshToken()
            );

        } catch (BadCredentialsException exception) {
            throw new BusinessException(
                    ErrorCode.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }
    }

    @Transactional
    public AuthResponse refresh(String rawRefreshToken) {
        RefreshTokenResult refreshToken =
                refreshTokenService.rotate(rawRefreshToken);
        User user = refreshToken.user();
        FluentoUserDetails userDetails = new FluentoUserDetails(user);
        String accessToken = jwtService.generateToken(userDetails);

        return buildAuthResponse(
                user,
                accessToken,
                refreshToken.refreshToken()
        );
    }

    @Transactional
    public void logout(String rawRefreshToken) {
        refreshTokenService.revoke(rawRefreshToken);
    }

    private AuthResponse buildAuthResponse(
            User user,
            String accessToken,
            String refreshToken
    ) {
        UserSummaryResponse userResponse =
                new UserSummaryResponse(
                        user.getId(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getRole(),
                        user.isEmailVerified()
                );

        return new AuthResponse(
                accessToken,
                refreshToken,
                "Bearer",
                jwtService.getExpirationSeconds(),
                userResponse
        );
    }

    private String normalizeEmail(String email) {
        return email
                .trim()
                .toLowerCase(Locale.ROOT);
    }
}
