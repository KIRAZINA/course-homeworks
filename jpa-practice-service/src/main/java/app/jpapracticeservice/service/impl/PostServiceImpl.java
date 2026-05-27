package app.jpapracticeservice.service.impl;

import app.jpapracticeservice.dto.PostCreateRequest;
import app.jpapracticeservice.dto.PostDto;
import app.jpapracticeservice.entity.Post;
import app.jpapracticeservice.entity.User;
import app.jpapracticeservice.exception.ResourceNotFoundException;
import app.jpapracticeservice.repository.PostRepository;
import app.jpapracticeservice.repository.UserRepository;
import app.jpapracticeservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public PostDto createPost(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.userId()));

        Post post = Post.builder()
                .title(request.title())
                .content(request.content())
                .user(user)
                .build();

        Post savedPost = postRepository.save(post);
        return mapToPostDto(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PostDto> findPostsByUserId(Long userId, Pageable pageable) {
        return postRepository.findByUserId(userId, pageable)
                .map(this::mapToPostDto);
    }

    private PostDto mapToPostDto(Post post) {
        return PostDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .userId(post.getUser() != null ? post.getUser().getId() : null)
                .build();
    }
}