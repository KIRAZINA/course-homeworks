package app.security;

import app.entity.Role;
import app.entity.User;
import app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock private UserRepository userRepository;
    @InjectMocks private CustomUserDetailsService userDetailsService;

    @Test
    @DisplayName("Should successfully load user by email and map authorities")
    void loadUserByUsername_Success() {
        // Arrange
        Role role = new Role("ROLE_USER");
        User user = new User();
        user.setEmail("admin@test.com");
        user.setPassword("hashed123");
        user.setRoles(Set.of(role));
        when(userRepository.findByEmail("admin@test.com")).thenReturn(Optional.of(user));

        // Act
        UserDetails userDetails = userDetailsService.loadUserByUsername("admin@test.com");

        // Assert
        assertThat(userDetails.getUsername()).isEqualTo("admin@test.com");
        assertThat(userDetails.getPassword()).isEqualTo("hashed123");
        assertThat(userDetails.getAuthorities()).extracting("authority").containsExactly("ROLE_USER");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user does not exist")
    void loadUserByUsername_UserNotFound() {
        // Arrange
        when(userRepository.findByEmail("missing@test.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("missing@test.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found with email: missing@test.com");
    }
}