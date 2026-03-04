package com.twitter.api.dto.response;

import com.twitter.api.entity.Role;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String email,
        String displayName,
        String bio,
        String profileImageUrl,
        Role role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
