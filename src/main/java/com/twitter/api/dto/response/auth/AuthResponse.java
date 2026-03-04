package com.twitter.api.dto.response.auth;

import com.twitter.api.entity.Role;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        Long userId,
        String username,
        String displayName,
        Role role
) {
}
