package com.fluento.features.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "First name is required")
        @Size(
                min = 2,
                max = 100,
                message = "First name must be between 2 and 100 characters"
        )
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(
                min = 2,
                max = 100,
                message = "Last name must be between 2 and 100 characters"
        )
        String lastName,

        @NotBlank(message = "Email is required")
        @Email(message = "Email address is invalid")
        @Size(
                max = 255,
                message = "Email must not exceed 255 characters"
        )
        String email,

        @NotBlank(message = "Password is required")
        @Size(
                min = 8,
                max = 72,
                message = "Password must be between 8 and 72 characters"
        )
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
                message = "Password must contain uppercase, lowercase, and number"
        )
        String password
) {
}