package app.jpapracticeservice.service.impl;

import app.jpapracticeservice.dto.*;
import app.jpapracticeservice.entity.Post;
import app.jpapracticeservice.entity.User;
import app.jpapracticeservice.exception.DuplicateResourceException;
import app.jpapracticeservice.exception.ResourceNotFoundException;
import app.jpapracticeservice.repository.PostRepository;
import app.jpapracticeservice.repository.UserRepository;
import app.jpapracticeservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    @Transactional
    public UserDto createUser(UserCreateRequest request) {
        try {
            User user = User.builder()
                    .name(request.name())
                    .email(request.email())
                    .build();
            User savedUser = userRepository.save(user);
            return mapToUserDto(savedUser);
        } catch (DataIntegrityViolationException ex) {
            throw new DuplicateResourceException("User", "email", request.email());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsersByName(String name) {
        return userRepository.findByNameIgnoreCase(name)
                .stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findUsersByNamePaginated(String name, Pageable pageable) {
        return userRepository.findByNameIgnoreCaseWithPosts(name, pageable)
                .map(this::mapToUserDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findUsersByEmailDomain(String domain) {
        String normalizedDomain = domain.startsWith("@") ? domain : "@" + domain;
        return userRepository.findByEmailEndingWithIgnoreCase(normalizedDomain)
                .stream()
                .map(this::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findUsersByEmailDomainPaginated(String domain, Pageable pageable) {
        String normalizedDomain = domain.startsWith("@") ? domain : "@" + domain;
        return userRepository.findByEmailEndingWithIgnoreCaseWithPosts(normalizedDomain, pageable)
                .map(this::mapToUserDto);
    }

    @Override
    @Transactional(readOnly = true)
    public UserWithPostsDto findUserWithPostsById(Long userId) {
        User user = userRepository.findByIdWithPosts(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        List<PostDto> postDtos = user.getPosts().stream()
                .map(this::mapToPostDto)
                .collect(Collectors.toList());

        return UserWithPostsDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .posts(postDtos)
                .build();
    }

    @Override
    @Transactional
    public void demonstrateTransactionalRollback(UserService.RollbackTestData data) {
        User user = User.builder()
                .name(data.userName())
                .email(data.userEmail())
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title(data.postTitle())
                .content(data.postContent())
                .build();
        user.addPost(post);
        postRepository.save(post);

        throw new RuntimeException("Simulated failure for transactional rollback demonstration");
    }

    private UserDto mapToUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
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