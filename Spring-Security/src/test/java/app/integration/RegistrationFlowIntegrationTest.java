package app.integration;

import app.dto.UserRegistrationDto;
import app.entity.User;
import app.repository.UserRepository;
import app.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class RegistrationFlowIntegrationTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @AfterEach
    void cleanUp() { userRepository.deleteAll(); }

    @Test
    @DisplayName("Full Registration Flow: DTO -> Service -> DB -> Verify Hash & Role")
    void fullRegistrationFlow() {
        // Arrange
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setName("Flow Test");
        dto.setEmail("flow@test.com");
        dto.setPhone("000");
        dto.setPassword("rawPassword123");

        // Act
        User saved = userService.registerUser(dto);

        // Assert
        Optional<User> dbUser = userRepository.findByEmail("flow@test.com");
        assertThat(dbUser).isPresent();

        User persisted = dbUser.get();
        assertThat(persisted.getName()).isEqualTo("Flow Test");
        assertThat(passwordEncoder.matches("rawPassword123", persisted.getPassword())).isTrue();
        assertThat(persisted.getRoles()).extracting("name").containsExactly("ROLE_USER");
    }
}