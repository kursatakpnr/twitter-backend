package com.twitter.api.exception;

public record FieldValidationError(
        String field,
        String message,
        String rejectedValue
) {
}
