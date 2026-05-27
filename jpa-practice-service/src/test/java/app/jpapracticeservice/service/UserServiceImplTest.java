package app.jpapracticeservice.service;

import app.jpapracticeservice.dto.UserCreateRequest;
import app.jpapracticeservice.dto.UserDto;
import app.jpapracticeservice.entity.User;
import app.jpapracticeservice.exception.DuplicateResourceException;
import app.jpapracticeservice.exception.ResourceNotFoundException;
import app.jpapracticeservice.repository.PostRepository;
import app.jpapracticeservice.repository.UserRepository;
import app.jpapracticeservice.service.impl.UserServiceImpl;
import app.jpapracticeservice.util.TestDataFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PostRepository postRepository;
    @InjectMocks private UserServiceImpl userService;

    private UserCreateRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new UserCreateRequest("Alice", "alice@domain.com");
    }

    @Test
    @DisplayName("Should create user successfully")
    void shouldCreateUserSuccessfully() {
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        UserDto result = userService.createUser(validRequest);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alice");
        assertThat(result.getEmail()).isEqualTo("alice@domain.com");
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on email constraint violation")
    void shouldThrowDuplicateExceptionOnEmailConstraint() {
        when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Unique constraint"));

        assertThatThrownBy(() -> userService.createUser(validRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("email");
    }

    @Test
    @DisplayName("Should normalize email domain before querying")
    void shouldNormalizeEmailDomainBeforeQuerying() {
        ArgumentCaptor<String> domainCaptor = ArgumentCaptor.forClass(String.class);
        when(userRepository.findByEmailEndingWithIgnoreCase(any())).thenReturn(List.of());

        userService.findUsersByEmailDomain("domain.com");

        verify(userRepository).findByEmailEndingWithIgnoreCase(domainCaptor.capture());
        assertThat(domainCaptor.getValue()).startsWith("@");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user not found with posts")
    void shouldThrowResourceNotFoundWhenUserMissing() {
        when(userRepository.findByIdWithPosts(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserWithPostsById(999L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should delegate pagination correctly")
    void shouldDelegatePaginationCorrectly() {
        Page<User> emptyEntityPage = new PageImpl<>(List.of(), TestDataFactory.pageable(0, 10), 0);
        when(userRepository.findByNameIgnoreCaseWithPosts(eq("test"), any())).thenReturn(emptyEntityPage);

        Page<UserDto> result = userService.findUsersByNamePaginated("test", TestDataFactory.pageable(0, 10));

        assertThat(result).isEmpty();
        verify(userRepository).findByNameIgnoreCaseWithPosts(eq("test"), any());
    }
}