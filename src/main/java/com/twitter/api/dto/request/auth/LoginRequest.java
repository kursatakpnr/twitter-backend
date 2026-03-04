package com.twitter.api.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank
        @Size(max = 150)
        String usernameOrEmail,

        @NotBlank
        @Size(min = 8, max = 72)
        String password
) {
}
