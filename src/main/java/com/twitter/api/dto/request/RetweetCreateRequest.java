package com.twitter.api.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RetweetCreateRequest(
        @NotNull
        Long tweetId,

        @Size(max = 280)
        String quoteText
) {
}
