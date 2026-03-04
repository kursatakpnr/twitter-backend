package com.twitter.api.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank
        @Size(min = 3, max = 30)
        @Pattern(regexp = "^[A-Za-z0-9_]+$")
        String username,

        @NotBlank
        @Email
        @Size(max = 150)
        String email,

        @NotBlank
        @Size(min = 8, max = 72)
        String password,

        @NotBlank
        @Size(min = 2, max = 100)
        String displayName,

        @Size(max = 160)
        String bio,

        @Size(max = 500)
        @Pattern(regexp = "^(https?://).*$")
        String profileImageUrl
) {
}
