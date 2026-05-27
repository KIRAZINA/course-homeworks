package app.jpapracticeservice.repository;

import app.jpapracticeservice.entity.Post;
import app.jpapracticeservice.entity.User;
import app.jpapracticeservice.util.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    @DisplayName("Should find users by name case-insensitively")
    void shouldFindUsersByNameCaseInsensitively() {
        // Use exact name for reliable matching
        User u1 = TestDataFactory.validUserWithName("Alice Search");
        User u2 = TestDataFactory.validUserWithName("alice search");
        User u3 = TestDataFactory.validUserWithName("Bob Other");

        entityManager.persistAndFlush(u1);
        entityManager.persistAndFlush(u2);
        entityManager.persistAndFlush(u3);

        // Search using exact name (case-insensitive query handles variation)
        List<User> results = userRepository.findByNameIgnoreCase("Alice Search");
        assertThat(results).hasSize(2);
    }

    @Test
    @DisplayName("Should find users by email domain suffix")
    void shouldFindUsersByEmailDomainSuffix() {
        entityManager.persistAndFlush(TestDataFactory.validUser("user1"));
        entityManager.persistAndFlush(TestDataFactory.validUser("user2"));
        entityManager.persistAndFlush(TestDataFactory.validUser("user3"));

        List<User> example = userRepository.findByEmailEndingWithIgnoreCase("@example.com");
        assertThat(example).hasSize(3);

        List<User> other = userRepository.findByEmailEndingWithIgnoreCase("@gmail.com");
        assertThat(other).isEmpty();
    }

    @Test
    @DisplayName("Should return user with posts using JOIN FETCH")
    void shouldReturnUserWithPostsUsingJoinFetch() {
        User user = TestDataFactory.validUser("john_fetch");
        entityManager.persistAndFlush(user);

        Post p1 = TestDataFactory.validPost(user);
        Post p2 = TestDataFactory.validPost(user);
        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(p2);

        entityManager.flush();
        entityManager.clear();

        Optional<User> found = userRepository.findByIdWithPosts(user.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getPosts()).hasSize(2);
    }

    @Test
    @DisplayName("Should paginate users correctly")
    void shouldPaginateUsersCorrectly() {
        // Use exact name match for pagination query
        String exactName = "PageUser Exact";
        for (int i = 0; i < 15; i++) {
            User user = TestDataFactory.validUserWithName(exactName);
            entityManager.persistAndFlush(user);
        }

        Pageable pageable = TestDataFactory.pageable(0, 5);
        Page<User> page = userRepository.findByNameIgnoreCaseWithPosts(exactName, pageable);

        assertThat(page.getContent()).hasSize(5);
        assertThat(page.getTotalElements()).isEqualTo(15);
        assertThat(page.hasNext()).isTrue();
    }

    @Test
    @DisplayName("Should handle empty query results gracefully")
    void shouldHandleEmptyQueryResults() {
        assertThat(userRepository.findByNameIgnoreCase("NonExistentUniqueName")).isEmpty();
        assertThat(userRepository.findByIdWithPosts(999L)).isEmpty();
    }
}