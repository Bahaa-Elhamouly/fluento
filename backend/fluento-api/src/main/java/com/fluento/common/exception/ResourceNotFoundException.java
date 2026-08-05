package com.fluento.common.exception;

import com.fluento.common.enums.ErrorCode;

public class ResourceNotFoundException extends BusinessException {

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(
                ErrorCode.RESOURCE_NOT_FOUND,
                "%s was not found with identifier: %s"
                        .formatted(resourceName, identifier)
        );
    }

    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}