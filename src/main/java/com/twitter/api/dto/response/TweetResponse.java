package com.twitter.api.dto.response;

import java.time.LocalDateTime;

public record TweetResponse(
        Long id,
        String content,
        String mediaUrl,
        UserSummaryResponse author,
        Long likeCount,
        Long dislikeCount,
        Long commentCount,
        Long retweetCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
