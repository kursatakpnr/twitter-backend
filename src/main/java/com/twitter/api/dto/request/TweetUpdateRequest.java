package com.twitter.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TweetUpdateRequest(
        @NotBlank
        @Size(max = 280)
        String content,

        @Size(max = 500)
        @Pattern(
                regexp = "^(https?://).*$|^$",
                message = "mediaUrl must start with http:// or https://"
        )
        String mediaUrl
) {
}
