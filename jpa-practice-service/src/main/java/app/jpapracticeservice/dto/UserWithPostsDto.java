package app.jpapracticeservice.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class UserWithPostsDto {
    private Long id;
    private String name;
    private String email;
    private List<PostDto> posts;
}