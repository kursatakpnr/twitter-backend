package com.twitter.api.dto.response;

import com.twitter.api.entity.ReactionType;

import java.time.LocalDateTime;

public record LikeResponse(
        Long id,
        Long tweetId,
        Long userId,
        ReactionType reactionType,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
