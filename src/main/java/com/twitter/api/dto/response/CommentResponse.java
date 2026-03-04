package com.twitter.api.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long tweetId,
        Long parentCommentId,
        String content,
        UserSummaryResponse author,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
