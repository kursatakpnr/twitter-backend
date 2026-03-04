package com.twitter.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CommentCreateRequest(
        @NotNull
        Long tweetId,

        Long parentCommentId,

        @NotBlank
        @Size(max = 280)
        String content
) {
}
