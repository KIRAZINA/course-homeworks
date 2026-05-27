package app.jpapracticeservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostCreateRequest(
        @NotBlank(message = "Title is required")
        String title,

        @NotBlank(message = "Content is required")
        String content,

        @NotNull(message = "User ID is required")
        Long userId
) {}