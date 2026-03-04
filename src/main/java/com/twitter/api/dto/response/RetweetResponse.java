package com.twitter.api.dto.response;

import java.time.LocalDateTime;

public record RetweetResponse(
        Long id,
        Long tweetId,
        Long userId,
        String quoteText,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
