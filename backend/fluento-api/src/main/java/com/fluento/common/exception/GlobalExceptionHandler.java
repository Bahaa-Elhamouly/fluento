package com.fluento.common.exception;

import com.fluento.common.enums.ErrorCode;
import com.fluento.common.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(
            BusinessException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = exception.getErrorCode();

        ApiError response = ApiError.of(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.name(),
                exception.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ) {
        Map<String, String> validationErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.putIfAbsent(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorCode errorCode = ErrorCode.VALIDATION_ERROR;

        ApiError response = ApiError.validation(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.name(),
                errorCode.getDefaultMessage(),
                request.getRequestURI(),
                validationErrors
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatchException(
            MethodArgumentTypeMismatchException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;

        String message = "Invalid value '%s' for parameter '%s'"
                .formatted(
                        exception.getValue(),
                        exception.getName()
                );

        ApiError response = ApiError.of(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.name(),
                message,
                request.getRequestURI()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;

        ApiError response = ApiError.of(
                errorCode.getStatus().value(),
                errorCode.getStatus().getReasonPhrase(),
                errorCode.name(),
                errorCode.getDefaultMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(errorCode.getStatus())
                .body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(
            Exception exception,
            HttpServletRequest request
    ) {
        log.error(
                "Unexpected error while processing request: {}",
                request.getRequestURI(),
                exception
        );

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        ApiError response = ApiError.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                errorCode.name(),
                errorCode.getDefaultMessage(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}