package app.jpapracticeservice.service;

import app.jpapracticeservice.repository.PostRepository;
import app.jpapracticeservice.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class UserServiceTransactionTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;
    @Autowired private PostRepository postRepository;
    @Autowired private EntityManager entityManager;

    @Test
    @DisplayName("Should rollback both user and post on RuntimeException")
    void shouldRollbackBothUserAndPostOnRuntimeException() {
        // Count before test - using direct query to avoid cache
        long initialUserCount = countUsersDirect();
        long initialPostCount = countPostsDirect();

        var data = new UserService.RollbackTestData(
                "RollbackUser_" + System.nanoTime(),
                "rb_" + System.nanoTime() + "_" + System.identityHashCode(this) + "@test.com",
                "RollPost", "Content");

        // Execute method that should trigger rollback
        assertThatThrownBy(() -> userService.demonstrateTransactionalRollback(data))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Simulated failure");

        // Verify counts after - using fresh queries
        long finalUserCount = countUsersDirect();
        long finalPostCount = countPostsDirect();

        assertThat(finalUserCount).isEqualTo(initialUserCount);
        assertThat(finalPostCount).isEqualTo(initialPostCount);
    }

    /**
     * Execute count query in a new transaction to see committed state.
     * This avoids reading uncommitted changes from the current transaction.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    long countUsersDirect() {
        return userRepository.count();
    }

    /**
     * Execute count query in a new transaction to see committed state.
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    long countPostsDirect() {
        return entityManager.createQuery("SELECT COUNT(p) FROM Post p", Long.class).getSingleResult();
    }
}