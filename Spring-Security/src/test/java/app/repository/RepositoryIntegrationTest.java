package app.repository;

import app.entity.Role;
import app.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class RepositoryIntegrationTest {

    @Autowired private TestEntityManager entityManager;
    @Autowired private UserRepository userRepository;
    @Autowired private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
        userRepository.deleteAll();
        entityManager.clear();

        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role role = new Role("ROLE_USER");
            entityManager.persist(role);
        }
        entityManager.flush();
    }

    @Test
    @DisplayName("UserRepository findByEmail should return user")
    void userRepository_FindByEmail() {
        // Arrange
        User user = new User();
        user.setName("Repo Test");
        user.setEmail("repo@test.com");
        user.setPhone("+1234567890");
        user.setPassword("hash");
        entityManager.persist(user);
        entityManager.flush();

        // Act
        Optional<User> found = userRepository.findByEmail("repo@test.com");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Repo Test");
    }

    @Test
    @DisplayName("UserRepository existsByEmail should return true/false correctly")
    void userRepository_ExistsByEmail() {
        // Arrange
        User user = new User();
        user.setName("Exists Test");
        user.setEmail("exists@test.com");
        user.setPhone("+1111111111");
        user.setPassword("hash");
        entityManager.persist(user);
        entityManager.flush();

        // Act & Assert
        assertThat(userRepository.existsByEmail("exists@test.com")).isTrue();
        assertThat(userRepository.existsByEmail("nonexistent@test.com")).isFalse();
    }

    @Test
    @DisplayName("RoleRepository findByName should return role uniquely")
    void roleRepository_FindByName() {
        // Act
        Optional<Role> found = roleRepository.findByName("ROLE_USER");

        // Assert
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("ROLE_USER");

        // Verify uniqueness: count should be 1
        long count = roleRepository.findAll().stream()
                .filter(r -> "ROLE_USER".equals(r.getName()))
                .count();
        assertThat(count).isEqualTo(1);
    }
}