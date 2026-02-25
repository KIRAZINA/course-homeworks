package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserRepositoryTest {

    private UserRepository repository;
    private User alice;
    private User bob;

    @BeforeEach
    void setUp() {
        repository = new UserRepository();

        alice = new User(1, "Alice", "Alice@Example.com");   // → alice@example.com
        bob   = new User(2, "Bob",   "bob@example.com");

        repository.addUser(alice);
        repository.addUser(bob);
    }

    @Test
    @DisplayName("findUserById should return user when ID exists")
    void findUserById_existingId_returnsUser() {
        Optional<User> result = repository.findUserById(1);

        assertThat(result)
                .isPresent()
                .hasValueSatisfying(user -> {
                    assertThat(user.getId()).isEqualTo(1);
                    assertThat(user.getName()).isEqualTo("Alice");
                    assertThat(user.getEmail()).isEqualTo("alice@example.com");
                });
    }

    @Test
    @DisplayName("findUserById should return empty when ID does not exist")
    void findUserById_nonExistingId_returnsEmpty() {
        Optional<User> result = repository.findUserById(999);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findUserByEmail should find user case-insensitively")
    void findUserByEmail_caseInsensitiveMatch() {
        Optional<User> result = repository.findUserByEmail("ALICE@EXAMPLE.COM");

        assertThat(result)
                .isPresent()
                .contains(alice);  // использует equals()
    }

    @Test
    @DisplayName("findUserByEmail should return empty for unknown email")
    void findUserByEmail_unknownEmail_returnsEmpty() {
        Optional<User> result = repository.findUserByEmail("unknown@domain.com");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findUserByEmail with null should return empty")
    void findUserByEmail_nullEmail_returnsEmpty() {
        Optional<User> result = repository.findUserByEmail(null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findAllUsers should return all added users")
    void findAllUsers_returnsAllUsers() {
        List<User> all = repository.findAllUsers();

        assertThat(all)
                .hasSize(2)
                .containsExactlyInAnyOrder(alice, bob);
    }

    @Test
    @DisplayName("findAllUsers should return unmodifiable list")
    void findAllUsers_returnsUnmodifiableList() {
        List<User> all = repository.findAllUsers();

        assertThatThrownBy(() -> all.add(new User(3, "Test", "test@example.com")))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    @DisplayName("addUser should throw exception when user is null")
    void addUser_nullUser_throwsException() {
        assertThatThrownBy(() -> repository.addUser(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("null");
    }

    @Test
    @DisplayName("User constructor should normalize email to lowercase")
    void userConstructor_normalizesEmail() {
        assertThat(alice.getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    @DisplayName("User constructor should throw on blank email")
    void userConstructor_blankEmail_throwsException() {
        assertThatThrownBy(() -> new User(5, "Test", "   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Empty repository returns empty list")
    void findAllUsers_emptyRepository_returnsEmptyList() {
        UserRepository emptyRepo = new UserRepository();
        assertThat(emptyRepo.findAllUsers()).isEmpty();
    }

    @Test
    @DisplayName("find methods on empty repository return empty Optional")
    void findMethods_onEmptyRepository_returnEmpty() {
        UserRepository emptyRepo = new UserRepository();
        assertThat(emptyRepo.findUserById(1)).isEmpty();
        assertThat(emptyRepo.findUserByEmail("test@example.com")).isEmpty();
    }

    @Test
    @DisplayName("Multiple users with same email - finds the first one")
    void findByEmail_multipleSameEmail_findsFirst() {
        repository.addUser(new User(3, "Alice2", "alice@example.com"));

        Optional<User> found = repository.findUserByEmail("alice@example.com");

        assertThat(found).isPresent().hasValueSatisfying(u ->
                assertThat(u.getId()).isEqualTo(1)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "ALICE@EXAMPLE.COM",
            "alice@Example.com ",
            "  Alice@Example.Com  "
    })
    @DisplayName("Email search is case-insensitive and trims")
    void findByEmail_variousFormats_findsUser(String emailInput) {
        Optional<User> found = repository.findUserByEmail(emailInput);
        assertThat(found).isPresent().contains(alice);
    }
}
