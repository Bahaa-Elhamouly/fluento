package com.fluento.features.admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fluento.common.enums.ErrorCode;
import com.fluento.common.exception.BusinessException;
import com.fluento.common.exception.ResourceNotFoundException;
import com.fluento.features.admin.dto.AdminCourseModuleRequest;
import com.fluento.features.admin.dto.AdminCourseRequest;
import com.fluento.features.admin.dto.AdminGrammarExampleRequest;
import com.fluento.features.admin.dto.AdminGrammarRuleRequest;
import com.fluento.features.admin.dto.AdminLessonRequest;
import com.fluento.features.admin.dto.AdminQuizQuestionRequest;
import com.fluento.features.admin.dto.AdminQuizRequest;
import com.fluento.features.admin.dto.AdminVocabularyTermRequest;
import com.fluento.features.course.dto.CourseModuleResponse;
import com.fluento.features.course.dto.CourseResponse;
import com.fluento.features.course.entity.Course;
import com.fluento.features.course.entity.CourseModule;
import com.fluento.features.course.entity.Language;
import com.fluento.features.course.repository.CourseModuleRepository;
import com.fluento.features.course.repository.CourseRepository;
import com.fluento.features.course.repository.LanguageRepository;
import com.fluento.features.grammar.dto.GrammarExampleResponse;
import com.fluento.features.grammar.dto.GrammarRuleDetailResponse;
import com.fluento.features.grammar.dto.GrammarRuleSummaryResponse;
import com.fluento.features.grammar.entity.GrammarExample;
import com.fluento.features.grammar.entity.GrammarRule;
import com.fluento.features.grammar.repository.GrammarExampleRepository;
import com.fluento.features.grammar.repository.GrammarRuleRepository;
import com.fluento.features.lesson.dto.LessonDetailResponse;
import com.fluento.features.lesson.entity.Lesson;
import com.fluento.features.lesson.repository.LessonRepository;
import com.fluento.features.quiz.dto.QuizQuestionResponse;
import com.fluento.features.quiz.dto.QuizResponse;
import com.fluento.features.quiz.entity.Quiz;
import com.fluento.features.quiz.entity.QuizQuestion;
import com.fluento.features.quiz.repository.QuizQuestionRepository;
import com.fluento.features.quiz.repository.QuizRepository;
import com.fluento.features.vocabulary.dto.VocabularyTermResponse;
import com.fluento.features.vocabulary.entity.VocabularyTerm;
import com.fluento.features.vocabulary.repository.VocabularyTermRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminContentService {

    private static final TypeReference<List<String>> STRING_LIST_TYPE =
            new TypeReference<>() {
            };

    private final LanguageRepository languageRepository;
    private final CourseRepository courseRepository;
    private final CourseModuleRepository courseModuleRepository;
    private final LessonRepository lessonRepository;
    private final VocabularyTermRepository vocabularyTermRepository;
    private final GrammarRuleRepository grammarRuleRepository;
    private final GrammarExampleRepository grammarExampleRepository;
    private final QuizRepository quizRepository;
    private final QuizQuestionRepository quizQuestionRepository;
    private final ObjectMapper objectMapper;

    @Transactional(readOnly = true)
    public List<CourseResponse> getCourses() {
        return courseRepository.findByDeletedAtIsNullOrderByPositionAscTitleAsc()
                .stream()
                .map(this::toCourseResponse)
                .toList();
    }

    @Transactional
    public CourseResponse createCourse(AdminCourseRequest request) {
        if (courseRepository.existsBySlugAndDeletedAtIsNull(normalizeSlug(request.slug()))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Course slug already exists");
        }

        Course course = new Course();
        applyCourseRequest(course, request);
        return toCourseResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse updateCourse(UUID courseId, AdminCourseRequest request) {
        Course course = getCourse(courseId);
        String requestedSlug = normalizeSlug(request.slug());
        if (!course.getSlug().equals(requestedSlug)
                && courseRepository.existsBySlugAndDeletedAtIsNull(requestedSlug)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Course slug already exists");
        }

        applyCourseRequest(course, request);
        return toCourseResponse(courseRepository.save(course));
    }

    @Transactional
    public void deleteCourse(UUID courseId) {
        Course course = getCourse(courseId);
        course.setDeletedAt(LocalDateTime.now());
        courseRepository.save(course);
    }

    @Transactional(readOnly = true)
    public List<CourseModuleResponse> getModules(UUID courseId) {
        if (courseRepository.findByIdAndDeletedAtIsNull(courseId).isEmpty()) {
            throw new ResourceNotFoundException("Course", courseId);
        }

        return courseModuleRepository
                .findByCourse_IdAndDeletedAtIsNullOrderByPositionAsc(courseId)
                .stream()
                .map(this::toModuleResponse)
                .toList();
    }

    @Transactional
    public CourseModuleResponse createModule(UUID courseId, AdminCourseModuleRequest request) {
        Course course = getCourse(courseId);
        CourseModule module = new CourseModule();
        module.setCourse(course);
        applyModuleRequest(module, request);
        return toModuleResponse(courseModuleRepository.save(module));
    }

    @Transactional
    public CourseModuleResponse updateModule(UUID moduleId, AdminCourseModuleRequest request) {
        CourseModule module = getModule(moduleId);
        applyModuleRequest(module, request);
        return toModuleResponse(courseModuleRepository.save(module));
    }

    @Transactional
    public void deleteModule(UUID moduleId) {
        CourseModule module = getModule(moduleId);
        module.setDeletedAt(LocalDateTime.now());
        courseModuleRepository.save(module);
    }

    @Transactional(readOnly = true)
    public List<LessonDetailResponse> getLessons(UUID moduleId) {
        if (courseModuleRepository.findByIdAndDeletedAtIsNull(moduleId).isEmpty()) {
            throw new ResourceNotFoundException("Course module", moduleId);
        }

        return lessonRepository
                .findByModule_IdAndDeletedAtIsNullOrderByPositionAsc(moduleId)
                .stream()
                .map(this::toLessonResponse)
                .toList();
    }

    @Transactional
    public LessonDetailResponse createLesson(UUID moduleId, AdminLessonRequest request) {
        CourseModule module = getModule(moduleId);
        Lesson lesson = new Lesson();
        lesson.setModule(module);
        applyLessonRequest(lesson, request);
        return toLessonResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public LessonDetailResponse updateLesson(UUID lessonId, AdminLessonRequest request) {
        Lesson lesson = getLesson(lessonId);
        applyLessonRequest(lesson, request);
        return toLessonResponse(lessonRepository.save(lesson));
    }

    @Transactional
    public void deleteLesson(UUID lessonId) {
        Lesson lesson = getLesson(lessonId);
        lesson.setDeletedAt(LocalDateTime.now());
        lessonRepository.save(lesson);
    }

    @Transactional(readOnly = true)
    public List<VocabularyTermResponse> getVocabularyTerms() {
        return vocabularyTermRepository.findByDeletedAtIsNullOrderByTermAsc()
                .stream()
                .map(this::toVocabularyResponse)
                .toList();
    }

    @Transactional
    public VocabularyTermResponse createVocabularyTerm(AdminVocabularyTermRequest request) {
        VocabularyTerm term = new VocabularyTerm();
        applyVocabularyRequest(term, request);
        return toVocabularyResponse(vocabularyTermRepository.save(term));
    }

    @Transactional
    public VocabularyTermResponse updateVocabularyTerm(UUID termId, AdminVocabularyTermRequest request) {
        VocabularyTerm term = getVocabularyTerm(termId);
        applyVocabularyRequest(term, request);
        return toVocabularyResponse(vocabularyTermRepository.save(term));
    }

    @Transactional
    public void deleteVocabularyTerm(UUID termId) {
        VocabularyTerm term = getVocabularyTerm(termId);
        term.setDeletedAt(LocalDateTime.now());
        vocabularyTermRepository.save(term);
    }

    @Transactional(readOnly = true)
    public List<GrammarRuleSummaryResponse> getGrammarRules() {
        return grammarRuleRepository.findByDeletedAtIsNullOrderByPositionAscTitleAsc()
                .stream()
                .map(this::toGrammarSummaryResponse)
                .toList();
    }

    @Transactional
    public GrammarRuleDetailResponse createGrammarRule(AdminGrammarRuleRequest request) {
        if (grammarRuleRepository.existsBySlugAndDeletedAtIsNull(normalizeSlug(request.slug()))) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Grammar rule slug already exists");
        }

        GrammarRule rule = new GrammarRule();
        applyGrammarRuleRequest(rule, request);
        GrammarRule savedRule = grammarRuleRepository.save(rule);
        replaceGrammarExamples(savedRule, request.examples());
        return toGrammarDetailResponse(savedRule);
    }

    @Transactional
    public GrammarRuleDetailResponse updateGrammarRule(UUID ruleId, AdminGrammarRuleRequest request) {
        GrammarRule rule = getGrammarRule(ruleId);
        String requestedSlug = normalizeSlug(request.slug());
        if (!rule.getSlug().equals(requestedSlug)
                && grammarRuleRepository.existsBySlugAndDeletedAtIsNull(requestedSlug)) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Grammar rule slug already exists");
        }

        applyGrammarRuleRequest(rule, request);
        GrammarRule savedRule = grammarRuleRepository.save(rule);
        replaceGrammarExamples(savedRule, request.examples());
        return toGrammarDetailResponse(savedRule);
    }

    @Transactional
    public void deleteGrammarRule(UUID ruleId) {
        GrammarRule rule = getGrammarRule(ruleId);
        LocalDateTime deletedAt = LocalDateTime.now();
        rule.setDeletedAt(deletedAt);
        grammarExampleRepository.findByRule_IdAndDeletedAtIsNullOrderByPositionAsc(ruleId)
                .forEach(example -> example.setDeletedAt(deletedAt));
        grammarRuleRepository.save(rule);
    }

    @Transactional(readOnly = true)
    public List<QuizResponse> getQuizzes(UUID lessonId) {
        if (lessonRepository.findByIdAndDeletedAtIsNull(lessonId).isEmpty()) {
            throw new ResourceNotFoundException("Lesson", lessonId);
        }

        return quizRepository.findByLesson_IdAndDeletedAtIsNullOrderByPositionAsc(lessonId)
                .stream()
                .map(this::toQuizResponse)
                .toList();
    }

    @Transactional
    public QuizResponse createQuiz(UUID lessonId, AdminQuizRequest request) {
        Lesson lesson = getLesson(lessonId);
        Quiz quiz = new Quiz();
        quiz.setLesson(lesson);
        applyQuizRequest(quiz, request);
        Quiz savedQuiz = quizRepository.save(quiz);
        replaceQuizQuestions(savedQuiz, request.questions());
        return toQuizResponse(savedQuiz);
    }

    @Transactional
    public QuizResponse updateQuiz(UUID quizId, AdminQuizRequest request) {
        Quiz quiz = getQuiz(quizId);
        applyQuizRequest(quiz, request);
        Quiz savedQuiz = quizRepository.save(quiz);
        replaceQuizQuestions(savedQuiz, request.questions());
        return toQuizResponse(savedQuiz);
    }

    @Transactional
    public void deleteQuiz(UUID quizId) {
        Quiz quiz = getQuiz(quizId);
        LocalDateTime deletedAt = LocalDateTime.now();
        quiz.setDeletedAt(deletedAt);
        quizQuestionRepository.findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quizId)
                .forEach(question -> question.setDeletedAt(deletedAt));
        quizRepository.save(quiz);
    }

    private void applyCourseRequest(Course course, AdminCourseRequest request) {
        Language language = languageRepository
                .findByCodeAndDeletedAtIsNull(request.languageCode().trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Language", request.languageCode()));

        course.setLanguage(language);
        course.setTitle(request.title().trim());
        course.setSlug(normalizeSlug(request.slug()));
        course.setDescription(request.description().trim());
        course.setLevel(request.level());
        course.setStatus(request.status());
        course.setPosition(request.position());
    }

    private void applyModuleRequest(CourseModule module, AdminCourseModuleRequest request) {
        module.setTitle(request.title().trim());
        module.setDescription(request.description() == null ? null : request.description().trim());
        module.setPosition(request.position());
    }

    private void applyLessonRequest(Lesson lesson, AdminLessonRequest request) {
        lesson.setTitle(request.title().trim());
        lesson.setDescription(request.description() == null ? null : request.description().trim());
        lesson.setType(request.type());
        lesson.setStatus(request.status());
        lesson.setContent(request.content().trim());
        lesson.setEstimatedMinutes(request.estimatedMinutes());
        lesson.setPosition(request.position());
    }

    private void applyVocabularyRequest(VocabularyTerm term, AdminVocabularyTermRequest request) {
        Language language = getLanguage(request.languageCode());
        term.setLanguage(language);
        term.setTerm(request.term().trim());
        term.setTranslation(request.translation().trim());
        term.setPronunciation(trimNullable(request.pronunciation()));
        term.setExample(trimNullable(request.example()));
        term.setTags(String.join(",", request.tags().stream().map(String::trim).toList()));
        term.setEnabled(request.enabled());
    }

    private void applyGrammarRuleRequest(GrammarRule rule, AdminGrammarRuleRequest request) {
        Language language = getLanguage(request.languageCode());
        rule.setLanguage(language);
        rule.setTitle(request.title().trim());
        rule.setSlug(normalizeSlug(request.slug()));
        rule.setExplanation(request.explanation().trim());
        rule.setLevel(request.level());
        rule.setEnabled(request.enabled());
        rule.setPosition(request.position());
    }

    private void replaceGrammarExamples(GrammarRule rule, List<AdminGrammarExampleRequest> examples) {
        LocalDateTime deletedAt = LocalDateTime.now();
        grammarExampleRepository.findByRule_IdAndDeletedAtIsNullOrderByPositionAsc(rule.getId())
                .forEach(example -> example.setDeletedAt(deletedAt));

        if (examples == null) {
            return;
        }

        examples.forEach(request -> {
            GrammarExample example = new GrammarExample();
            example.setRule(rule);
            example.setSourceText(request.sourceText().trim());
            example.setTranslation(request.translation().trim());
            example.setNote(trimNullable(request.note()));
            example.setPosition(request.position());
            grammarExampleRepository.save(example);
        });
    }

    private void applyQuizRequest(Quiz quiz, AdminQuizRequest request) {
        quiz.setTitle(request.title().trim());
        quiz.setDescription(trimNullable(request.description()));
        quiz.setStatus(request.status());
        quiz.setPassingScore(request.passingScore());
        quiz.setPosition(request.position());
    }

    private void replaceQuizQuestions(Quiz quiz, List<AdminQuizQuestionRequest> questions) {
        LocalDateTime deletedAt = LocalDateTime.now();
        quizQuestionRepository.findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId())
                .forEach(question -> question.setDeletedAt(deletedAt));

        if (questions == null) {
            return;
        }

        questions.forEach(request -> {
            QuizQuestion question = new QuizQuestion();
            question.setQuiz(quiz);
            question.setType(request.type());
            question.setPrompt(request.prompt().trim());
            question.setOptionsJson(toOptionsJson(request.options()));
            question.setCorrectAnswer(request.correctAnswer().trim());
            question.setExplanation(trimNullable(request.explanation()));
            question.setPoints(request.points());
            question.setPosition(request.position());
            quizQuestionRepository.save(question);
        });
    }

    private Language getLanguage(String languageCode) {
        return languageRepository
                .findByCodeAndDeletedAtIsNull(languageCode.trim().toLowerCase())
                .orElseThrow(() -> new ResourceNotFoundException("Language", languageCode));
    }

    private Course getCourse(UUID courseId) {
        return courseRepository.findByIdAndDeletedAtIsNull(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", courseId));
    }

    private CourseModule getModule(UUID moduleId) {
        return courseModuleRepository.findByIdAndDeletedAtIsNull(moduleId)
                .orElseThrow(() -> new ResourceNotFoundException("Course module", moduleId));
    }

    private Lesson getLesson(UUID lessonId) {
        return lessonRepository.findByIdAndDeletedAtIsNull(lessonId)
                .orElseThrow(() -> new ResourceNotFoundException("Lesson", lessonId));
    }

    private VocabularyTerm getVocabularyTerm(UUID termId) {
        return vocabularyTermRepository.findByIdAndDeletedAtIsNull(termId)
                .orElseThrow(() -> new ResourceNotFoundException("Vocabulary term", termId));
    }

    private GrammarRule getGrammarRule(UUID ruleId) {
        return grammarRuleRepository.findByIdAndDeletedAtIsNull(ruleId)
                .orElseThrow(() -> new ResourceNotFoundException("Grammar rule", ruleId));
    }

    private Quiz getQuiz(UUID quizId) {
        return quizRepository.findByIdAndDeletedAtIsNull(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", quizId));
    }

    private String normalizeSlug(String slug) {
        return slug.trim().toLowerCase();
    }

    private String trimNullable(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }

    private CourseResponse toCourseResponse(Course course) {
        return new CourseResponse(
                course.getId(),
                course.getLanguage().getCode(),
                course.getLanguage().getName(),
                course.getTitle(),
                course.getSlug(),
                course.getDescription(),
                course.getLevel(),
                course.getStatus(),
                course.getPosition()
        );
    }

    private CourseModuleResponse toModuleResponse(CourseModule module) {
        return new CourseModuleResponse(
                module.getId(),
                module.getCourse().getId(),
                module.getTitle(),
                module.getDescription(),
                module.getPosition()
        );
    }

    private LessonDetailResponse toLessonResponse(Lesson lesson) {
        return new LessonDetailResponse(
                lesson.getId(),
                lesson.getModule().getId(),
                lesson.getModule().getCourse().getId(),
                lesson.getTitle(),
                lesson.getDescription(),
                lesson.getType(),
                lesson.getStatus(),
                lesson.getContent(),
                lesson.getEstimatedMinutes(),
                lesson.getPosition()
        );
    }

    private VocabularyTermResponse toVocabularyResponse(VocabularyTerm term) {
        return new VocabularyTermResponse(
                term.getId(),
                term.getLanguage().getCode(),
                term.getTerm(),
                term.getTranslation(),
                term.getPronunciation(),
                term.getExample(),
                parseList(term.getTags())
        );
    }

    private GrammarRuleSummaryResponse toGrammarSummaryResponse(GrammarRule rule) {
        return new GrammarRuleSummaryResponse(
                rule.getId(),
                rule.getLanguage().getCode(),
                rule.getTitle(),
                rule.getSlug(),
                rule.getLevel(),
                rule.getPosition()
        );
    }

    private GrammarRuleDetailResponse toGrammarDetailResponse(GrammarRule rule) {
        List<GrammarExampleResponse> examples = grammarExampleRepository
                .findByRule_IdAndDeletedAtIsNullOrderByPositionAsc(rule.getId())
                .stream()
                .map(this::toGrammarExampleResponse)
                .toList();

        return new GrammarRuleDetailResponse(
                rule.getId(),
                rule.getLanguage().getCode(),
                rule.getTitle(),
                rule.getSlug(),
                rule.getExplanation(),
                rule.getLevel(),
                rule.getPosition(),
                examples
        );
    }

    private GrammarExampleResponse toGrammarExampleResponse(GrammarExample example) {
        return new GrammarExampleResponse(
                example.getSourceText(),
                example.getTranslation(),
                example.getNote(),
                example.getPosition()
        );
    }

    private QuizResponse toQuizResponse(Quiz quiz) {
        List<QuizQuestionResponse> questions = quizQuestionRepository
                .findByQuiz_IdAndDeletedAtIsNullOrderByPositionAsc(quiz.getId())
                .stream()
                .map(this::toQuizQuestionResponse)
                .toList();

        return new QuizResponse(
                quiz.getId(),
                quiz.getLesson().getId(),
                quiz.getTitle(),
                quiz.getDescription(),
                quiz.getPassingScore(),
                quiz.getPosition(),
                questions
        );
    }

    private QuizQuestionResponse toQuizQuestionResponse(QuizQuestion question) {
        return new QuizQuestionResponse(
                question.getId(),
                question.getType(),
                question.getPrompt(),
                parseOptions(question.getOptionsJson()),
                question.getPoints(),
                question.getPosition()
        );
    }

    private List<String> parseList(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        return List.of(value.split(","))
                .stream()
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }

    private String toOptionsJson(List<String> options) {
        List<String> safeOptions = options == null
                ? List.of()
                : options.stream().map(String::trim).filter(option -> !option.isBlank()).toList();

        try {
            return objectMapper.writeValueAsString(safeOptions);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Quiz question options could not be serialized", exception);
        }
    }

    private List<String> parseOptions(String optionsJson) {
        if (optionsJson == null || optionsJson.isBlank()) {
            return List.of();
        }

        try {
            return objectMapper.readValue(optionsJson, STRING_LIST_TYPE);
        } catch (JsonProcessingException exception) {
            throw new IllegalStateException("Quiz question options are not valid JSON", exception);
        }
    }
}
