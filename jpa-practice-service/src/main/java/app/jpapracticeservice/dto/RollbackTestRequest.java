package app.jpapracticeservice.dto;

import jakarta.validation.constraints.NotBlank;

public record RollbackTestRequest(
        @NotBlank(message = "User name is required")
        String userName,

        @NotBlank(message = "User email is required")
        String userEmail,

        @NotBlank(message = "Post title is required")
        String postTitle,

        @NotBlank(message = "Post content is required")
        String postContent
) {}