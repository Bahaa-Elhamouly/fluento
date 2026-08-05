package com.fluento.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    VALIDATION_ERROR(
            HttpStatus.BAD_REQUEST,
            "Validation failed"
    ),

    BAD_REQUEST(
            HttpStatus.BAD_REQUEST,
            "Invalid request"
    ),

    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "Authentication is required"
    ),

    ACCESS_DENIED(
            HttpStatus.FORBIDDEN,
            "You do not have permission to perform this action"
    ),

    RESOURCE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "Requested resource was not found"
    ),

    EMAIL_ALREADY_EXISTS(
            HttpStatus.CONFLICT,
            "Email address is already registered"
    ),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected error occurred"
    );

    private final HttpStatus status;
    private final String defaultMessage;
}