package com.fluento.features.ai.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.ai.dto.AiSessionResponse;
import com.fluento.features.ai.dto.CreateAiSessionRequest;
import com.fluento.features.ai.dto.SendAiMessageRequest;
import com.fluento.features.ai.entity.AiConversationMessage;
import com.fluento.features.ai.entity.AiConversationSession;
import com.fluento.features.ai.repository.AiConversationMessageRepository;
import com.fluento.features.ai.repository.AiConversationSessionRepository;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.enums.Role;
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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiTutorServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private AiConversationSessionRepository sessionRepository;

    @Mock
    private AiConversationMessageRepository messageRepository;

    @InjectMocks
    private AiTutorService aiTutorService;

    @Test
    void createSessionCreatesOwnedLanguageSession() {
        User user = user();
        Language language = language();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(languageRepository.findByCodeAndDeletedAtIsNull("de"))
                .thenReturn(Optional.of(language));
        when(sessionRepository.save(any(AiConversationSession.class)))
                .thenAnswer(invocation -> {
                    AiConversationSession session = invocation.getArgument(0);
                    session.setId(UUID.randomUUID());
                    return session;
                });

        AiSessionResponse response = aiTutorService.createSession(
                user.getId(),
                new CreateAiSessionRequest(" DE ", null)
        );

        assertThat(response.languageCode()).isEqualTo("de");
        assertThat(response.title()).isEqualTo("German AI Tutor");
        assertThat(response.messages()).isEmpty();
    }

    @Test
    void createSessionThrowsWhenLanguageIsMissing() {
        User user = user();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(languageRepository.findByCodeAndDeletedAtIsNull("it"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(
                () -> aiTutorService.createSession(
                        user.getId(),
                        new CreateAiSessionRequest("it", null)
                )
        )
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void sendMessageStoresUserAndTutorMessages() {
        User user = user();
        AiConversationSession session = session(user, language());

        when(sessionRepository
                .findByIdAndUser_IdAndActiveTrueAndDeletedAtIsNull(
                        session.getId(),
                        user.getId()
                ))
                .thenReturn(Optional.of(session));
        when(messageRepository
                .findBySession_IdAndDeletedAtIsNullOrderByCreatedAtAsc(
                        session.getId()
                ))
                .thenReturn(List.of());

        aiTutorService.sendMessage(
                user.getId(),
                session.getId(),
                new SendAiMessageRequest("Hallo")
        );

        verify(messageRepository, times(2))
                .save(any(AiConversationMessage.class));
    }

    @Test
    void getSessionThrowsWhenSessionIsNotOwned() {
        UUID userId = UUID.randomUUID();
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository
                .findByIdAndUser_IdAndActiveTrueAndDeletedAtIsNull(
                        sessionId,
                        userId
                ))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> aiTutorService.getSession(userId, sessionId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    private AiConversationSession session(User user, Language language) {
        AiConversationSession session = new AiConversationSession();
        session.setId(UUID.randomUUID());
        session.setUser(user);
        session.setLanguage(language);
        session.setTitle("German AI Tutor");
        session.setActive(true);
        return session;
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

    private Language language() {
        Language language = new Language();
        language.setId(UUID.randomUUID());
        language.setCode("de");
        language.setName("German");
        language.setNativeName("Deutsch");
        language.setEnabled(true);
        return language;
    }
}
