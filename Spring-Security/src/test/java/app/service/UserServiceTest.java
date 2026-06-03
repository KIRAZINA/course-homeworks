package app.service;

import app.dto.UserRegistrationDto;
import app.entity.Role;
import app.entity.User;
import app.repository.RoleRepository;
import app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    private UserRegistrationDto registrationDto;

    @BeforeEach
    void setUp() {
        registrationDto = new UserRegistrationDto();
        registrationDto.setName("Test User");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPhone("+1234567890");
        registrationDto.setPassword("securePass123");
    }

    @Test
    @DisplayName("Should successfully register a new user with hashed password and ROLE_USER")
    void registerUser_Success() {
        // Arrange
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        Role existingRole = new Role("ROLE_USER");
        existingRole.setId(1L);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(existingRole));
        when(passwordEncoder.encode("securePass123")).thenReturn("$2a$10$hashedValue");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.registerUser(registrationDto);

        // Assert
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getPassword()).isEqualTo("$2a$10$hashedValue");
        assertThat(savedUser.getRoles()).hasSize(1);
        assertThat(savedUser.getRoles().iterator().next().getName()).isEqualTo("ROLE_USER");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Should create ROLE_USER if it does not exist")
    void registerUser_CreatesDefaultRoleIfMissing() {
        // Arrange
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());
        Role newRole = new Role("ROLE_USER");
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);
        when(passwordEncoder.encode("securePass123")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userService.registerUser(registrationDto);

        // Assert
        verify(roleRepository).save(any(Role.class));
        assertThat(savedUser.getRoles().iterator().next()).isEqualTo(newRole);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when email already exists")
    void registerUser_DuplicateEmailThrowsException() {
        // Arrange
        when(userRepository.existsByEmail(registrationDto.getEmail())).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.registerUser(registrationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email is already registered");
        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should retrieve all registered users")
    void getAllUsers_ReturnsList() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("user1@test.com");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo("user1@test.com");
    }
}