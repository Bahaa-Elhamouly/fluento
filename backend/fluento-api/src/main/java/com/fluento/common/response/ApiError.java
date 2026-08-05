package com.fluento.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
        boolean success,
        int status,
        String error,
        String code,
        String message,
        String path,
        Map<String, String> validationErrors,
        Instant timestamp
) {

    public static ApiError of(
            int status,
            String error,
            String code,
            String message,
            String path
    ) {
        return new ApiError(
                false,
                status,
                error,
                code,
                message,
                path,
                null,
                Instant.now()
        );
    }

    public static ApiError validation(
            int status,
            String error,
            String code,
            String message,
            String path,
            Map<String, String> validationErrors
    ) {
        return new ApiError(
                false,
                status,
                error,
                code,
                message,
                path,
                validationErrors,
                Instant.now()
        );
    }
}