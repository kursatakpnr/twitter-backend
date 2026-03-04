package com.twitter.api.dto.response;

public record UserSummaryResponse(
        Long id,
        String username,
        String displayName,
        String profileImageUrl
) {
}
