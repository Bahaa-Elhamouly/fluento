package com.fluento.features.auth.service;

import com.fluento.common.enums.ErrorCode;
import com.fluento.common.exception.BusinessException;
import com.fluento.features.auth.dto.request.LoginRequest;
import com.fluento.features.auth.dto.request.RegisterRequest;
import com.fluento.features.auth.dto.response.AuthResponse;
import com.fluento.features.auth.security.FluentoUserDetails;
import com.fluento.features.auth.security.JwtService;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
import com.fluento.features.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthService authService;

    @Test
    void registerCreatesNormalizedUserAndReturnsToken() {
        RegisterRequest request = new RegisterRequest(
                " Sara ",
                " Ahmed ",
                " SARA@Example.COM ",
                "Password1"
        );

        User savedUser = user(
                UUID.randomUUID(),
                "Sara",
                "Ahmed",
                "sara@example.com"
        );

        when(userRepository.existsByEmailIgnoreCaseAndDeletedAtIsNull(
                "sara@example.com"
        )).thenReturn(false);
        when(passwordEncoder.encode("Password1"))
                .thenReturn("encoded-password");
        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);
        when(jwtService.generateToken(any(FluentoUserDetails.class)))
                .thenReturn("access-token");
        when(refreshTokenService.create(savedUser))
                .thenReturn(
                        new RefreshTokenResult(
                                "refresh-token",
                                savedUser
                        )
                );
        when(jwtService.getExpirationSeconds())
                .thenReturn(3600L);

        AuthResponse response = authService.register(request);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User userToSave = userCaptor.getValue();
        assertThat(userToSave.getFirstName()).isEqualTo("Sara");
        assertThat(userToSave.getLastName()).isEqualTo("Ahmed");
        assertThat(userToSave.getEmail()).isEqualTo("sara@example.com");
        assertThat(userToSave.getPasswordHash())
                .isEqualTo("encoded-password");
        assertThat(userToSave.getRole()).isEqualTo(Role.USER);
        assertThat(userToSave.isEnabled()).isTrue();
        assertThat(userToSave.isEmailVerified()).isFalse();

        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.tokenType()).isEqualTo("Bearer");
        assertThat(response.expiresIn()).isEqualTo(3600L);
        assertThat(response.user().email()).isEqualTo("sara@example.com");
    }

    @Test
    void registerRejectsDuplicateActiveEmail() {
        RegisterRequest request = new RegisterRequest(
                "Sara",
                "Ahmed",
                "sara@example.com",
                "Password1"
        );

        when(userRepository.existsByEmailIgnoreCaseAndDeletedAtIsNull(
                "sara@example.com"
        )).thenReturn(true);

        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> assertThat(exception.getErrorCode())
                                .isEqualTo(ErrorCode.EMAIL_ALREADY_EXISTS)
                );
    }

    @Test
    void loginAuthenticatesNormalizedEmailAndReturnsToken() {
        LoginRequest request = new LoginRequest(
                " SARA@Example.COM ",
                "Password1"
        );

        User user = user(
                UUID.randomUUID(),
                "Sara",
                "Ahmed",
                "sara@example.com"
        );
        FluentoUserDetails userDetails = new FluentoUserDetails(user);

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userRepository.findByEmailIgnoreCaseAndDeletedAtIsNull(
                "sara@example.com"
        )).thenReturn(Optional.of(user));
        when(jwtService.generateToken(eq(userDetails)))
                .thenReturn("access-token");
        when(refreshTokenService.create(user))
                .thenReturn(new RefreshTokenResult("refresh-token", user));
        when(jwtService.getExpirationSeconds())
                .thenReturn(3600L);

        AuthResponse response = authService.login(request);

        ArgumentCaptor<UsernamePasswordAuthenticationToken>
                authenticationCaptor = ArgumentCaptor.forClass(
                UsernamePasswordAuthenticationToken.class
        );
        verify(authenticationManager).authenticate(
                authenticationCaptor.capture()
        );

        assertThat(authenticationCaptor.getValue().getPrincipal())
                .isEqualTo("sara@example.com");
        assertThat(authenticationCaptor.getValue().getCredentials())
                .isEqualTo("Password1");
        assertThat(response.accessToken()).isEqualTo("access-token");
        assertThat(response.refreshToken()).isEqualTo("refresh-token");
        assertThat(response.user().email()).isEqualTo("sara@example.com");
    }

    @Test
    void loginMapsBadCredentialsToBusinessException() {
        LoginRequest request = new LoginRequest(
                "sara@example.com",
                "wrong-password"
        );

        when(authenticationManager.authenticate(any(
                UsernamePasswordAuthenticationToken.class
        ))).thenThrow(new BadCredentialsException("bad credentials"));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOfSatisfying(
                        BusinessException.class,
                        exception -> {
                            assertThat(exception.getErrorCode())
                                    .isEqualTo(ErrorCode.UNAUTHORIZED);
                            assertThat(exception.getMessage())
                                    .isEqualTo("Invalid email or password");
                        }
                );
    }

    @Test
    void refreshRotatesRefreshTokenAndReturnsNewAccessToken() {
        User user = user(
                UUID.randomUUID(),
                "Sara",
                "Ahmed",
                "sara@example.com"
        );

        when(refreshTokenService.rotate("old-refresh-token"))
                .thenReturn(
                        new RefreshTokenResult(
                                "new-refresh-token",
                                user
                        )
                );
        when(jwtService.generateToken(any(FluentoUserDetails.class)))
                .thenReturn("new-access-token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        AuthResponse response = authService.refresh("old-refresh-token");

        assertThat(response.accessToken()).isEqualTo("new-access-token");
        assertThat(response.refreshToken()).isEqualTo("new-refresh-token");
        assertThat(response.user().email()).isEqualTo("sara@example.com");
    }

    @Test
    void logoutRevokesRefreshToken() {
        authService.logout("refresh-token");

        verify(refreshTokenService).revoke("refresh-token");
    }

    private User user(
            UUID id,
            String firstName,
            String lastName,
            String email
    ) {
        User user = new User();
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPasswordHash("encoded-password");
        user.setRole(Role.USER);
        user.setEnabled(true);
        user.setEmailVerified(false);
        return user;
    }
}
