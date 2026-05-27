package app.jpapracticeservice.service;

import app.jpapracticeservice.dto.PostCreateRequest;
import app.jpapracticeservice.dto.PostDto;
import app.jpapracticeservice.entity.Post;
import app.jpapracticeservice.entity.User;
import app.jpapracticeservice.exception.ResourceNotFoundException;
import app.jpapracticeservice.repository.PostRepository;
import app.jpapracticeservice.repository.UserRepository;
import app.jpapracticeservice.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {

    @Mock private PostRepository postRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private PostServiceImpl postService;

    private PostCreateRequest validRequest;
    private User existingUser;

    @BeforeEach
    void setUp() {
        validRequest = new PostCreateRequest("Title", "Content", 1L);
        existingUser = User.builder().id(1L).name("Author").build();
    }

    @Test
    @DisplayName("Should create post successfully")
    void shouldCreatePostSuccessfully() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post post = invocation.getArgument(0);
            post.setId(10L);
            return post;
        });

        PostDto result = postService.createPost(validRequest);

        assertThat(result.getId()).isEqualTo(10L);
        assertThat(result.getUserId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for invalid user ID")
    void shouldThrowResourceNotFoundForInvalidUserId() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> postService.createPost(new PostCreateRequest("T", "C", 999L)))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(postRepository, never()).save(any());
    }
}