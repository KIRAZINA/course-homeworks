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

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PostRepositoryTest {

    @Autowired private PostRepository postRepository;
    @Autowired private TestEntityManager entityManager;

    @Test
    @DisplayName("Should find posts by user ID with JOIN FETCH")
    void shouldFindPostsByUserIdWithJoinFetch() {
        User user = TestDataFactory.validUser("postowner");
        entityManager.persistAndFlush(user);

        Post p1 = TestDataFactory.validPost(user);
        Post p2 = TestDataFactory.validPost(user);
        entityManager.persistAndFlush(p1);
        entityManager.persistAndFlush(p2);

        Pageable pageable = TestDataFactory.pageable(0, 10);
        Page<Post> posts = postRepository.findByUserId(user.getId(), pageable);

        assertThat(posts.getContent()).hasSize(2);
        assertThat(posts.getContent().get(0).getUser().getName()).isNotBlank();
    }

    @Test
    @DisplayName("Should return empty page for non-existent user ID")
    void shouldReturnEmptyPageForNonExistentUserId() {
        Page<Post> posts = postRepository.findByUserId(999L, TestDataFactory.pageable(0, 10));
        assertThat(posts.isEmpty()).isTrue();
        assertThat(posts.getTotalElements()).isZero();
    }
}