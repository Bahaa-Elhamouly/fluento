package com.fluento.features.ai.service;

import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.ai.dto.AiMessageResponse;
import com.fluento.features.ai.dto.AiSessionResponse;
import com.fluento.features.ai.dto.CreateAiSessionRequest;
import com.fluento.features.ai.dto.SendAiMessageRequest;
import com.fluento.features.ai.entity.AiConversationMessage;
import com.fluento.features.ai.entity.AiConversationSession;
import com.fluento.features.ai.enums.AiMessageRole;
import com.fluento.features.ai.repository.AiConversationMessageRepository;
import com.fluento.features.ai.repository.AiConversationSessionRepository;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.user.entity.User;
import com.fluento.features.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiTutorService {

    private static final String LOCAL_PROVIDER = "local";
    private static final String LOCAL_MODEL = "fluento-rule-based-tutor-v1";

    private final UserRepository userRepository;
    private final LanguageRepository languageRepository;
    private final AiConversationSessionRepository sessionRepository;
    private final AiConversationMessageRepository messageRepository;

    @Transactional(readOnly = true)
    public List<AiSessionResponse> getSessions(UUID userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", userId);
        }

        return sessionRepository
                .findTop20ByUser_IdAndActiveTrueAndDeletedAtIsNullOrderByUpdatedAtDesc(
                        userId
                )
                .stream()
                .map(session -> toSessionResponse(session, List.of()))
                .toList();
    }

    @Transactional
    public AiSessionResponse createSession(
            UUID userId,
            CreateAiSessionRequest request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Language language = languageRepository
                .findByCodeAndDeletedAtIsNull(
                        request.languageCode().trim().toLowerCase()
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Language",
                                request.languageCode()
                        )
                );

        AiConversationSession session = new AiConversationSession();
        session.setUser(user);
        session.setLanguage(language);
        session.setTitle(request.title() == null || request.title().isBlank()
                ? language.getName() + " AI Tutor"
                : request.title().trim());
        session.setActive(true);

        return toSessionResponse(sessionRepository.save(session), List.of());
    }

    @Transactional(readOnly = true)
    public AiSessionResponse getSession(UUID userId, UUID sessionId) {
        AiConversationSession session = getOwnedSession(userId, sessionId);

        return toSessionResponse(
                session,
                messageRepository
                        .findBySession_IdAndDeletedAtIsNullOrderByCreatedAtAsc(
                                sessionId
                        )
        );
    }

    @Transactional
    public AiSessionResponse sendMessage(
            UUID userId,
            UUID sessionId,
            SendAiMessageRequest request
    ) {
        AiConversationSession session = getOwnedSession(userId, sessionId);

        AiConversationMessage userMessage = new AiConversationMessage();
        userMessage.setSession(session);
        userMessage.setRole(AiMessageRole.USER);
        userMessage.setContent(request.message().trim());
        userMessage.setProvider(LOCAL_PROVIDER);
        userMessage.setModel(LOCAL_MODEL);
        messageRepository.save(userMessage);

        AiConversationMessage tutorMessage = new AiConversationMessage();
        tutorMessage.setSession(session);
        tutorMessage.setRole(AiMessageRole.TUTOR);
        tutorMessage.setContent(buildTutorReply(session, request.message()));
        tutorMessage.setProvider(LOCAL_PROVIDER);
        tutorMessage.setModel(LOCAL_MODEL);
        messageRepository.save(tutorMessage);

        return getSession(userId, sessionId);
    }

    private AiConversationSession getOwnedSession(
            UUID userId,
            UUID sessionId
    ) {
        return sessionRepository
                .findByIdAndUser_IdAndActiveTrueAndDeletedAtIsNull(
                        sessionId,
                        userId
                )
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "AI conversation session",
                                sessionId
                        )
                );
    }

    private String buildTutorReply(
            AiConversationSession session,
            String message
    ) {
        return "Let's practice %s. You wrote: \"%s\". Try turning it into a short sentence, then compare grammar, vocabulary, and pronunciation."
                .formatted(
                        session.getLanguage().getName(),
                        message.trim()
                );
    }

    private AiSessionResponse toSessionResponse(
            AiConversationSession session,
            List<AiConversationMessage> messages
    ) {
        return new AiSessionResponse(
                session.getId(),
                session.getLanguage().getCode(),
                session.getTitle(),
                session.isActive(),
                session.getCreatedAt(),
                session.getUpdatedAt(),
                messages.stream()
                        .map(this::toMessageResponse)
                        .toList()
        );
    }

    private AiMessageResponse toMessageResponse(
            AiConversationMessage message
    ) {
        return new AiMessageResponse(
                message.getId(),
                message.getRole(),
                message.getContent(),
                message.getProvider(),
                message.getModel(),
                message.getCreatedAt()
        );
    }
}
