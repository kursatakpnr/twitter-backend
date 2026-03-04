package com.twitter.api.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 2, max = 100)
        String displayName,

        @Size(max = 160)
        String bio,

        @Size(max = 500)
        @Pattern(regexp = "^(https?://).*$")
        String profileImageUrl
) {
}
