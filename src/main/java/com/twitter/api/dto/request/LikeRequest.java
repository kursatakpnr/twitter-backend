package com.twitter.api.dto.request;

import com.twitter.api.entity.ReactionType;
import jakarta.validation.constraints.NotNull;

public record LikeRequest(
        @NotNull
        Long tweetId,

        @NotNull
        ReactionType reactionType
) {
}
