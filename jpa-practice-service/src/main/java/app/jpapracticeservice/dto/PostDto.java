package app.jpapracticeservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class PostDto {
    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private Long userId;
}