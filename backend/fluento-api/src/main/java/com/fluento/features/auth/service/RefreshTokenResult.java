package com.fluento.features.auth.service;

import com.fluento.features.user.entity.User;

public record RefreshTokenResult(
        String refreshToken,
        User user
) {
}
