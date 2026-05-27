package app.jpapracticeservice.util;

import app.jpapracticeservice.dto.UserCreateRequest;
import app.jpapracticeservice.entity.Post;
import app.jpapracticeservice.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public final class TestDataFactory {

    private TestDataFactory() {}

    private static String uniqueSuffix() {
        return UUID.randomUUID().toString().replace("-", "") + "_" +
                System.nanoTime() + "_" + Thread.currentThread().getId();
    }

    public static User validUser() {
        return validUser("User_" + uniqueSuffix());
    }

    public static User validUser(String identifier) {
        String safeId = identifier.toLowerCase().replaceAll("[^a-z0-9@._-]", "_");
        return User.builder()
                .name("John " + identifier)
                .email(safeId + "@example.com")
                .build();
    }

    public static User validUserWithName(String exactName) {
        String safeName = exactName.toLowerCase().replaceAll("[^a-z0-9@._-]", "_");
        return User.builder()
                .name(exactName)
                .email(safeName + "_" + uniqueSuffix() + "@example.com")
                .build();
    }

    public static Post validPost(User user) {
        return Post.builder()
                .title("Post_" + uniqueSuffix())
                .content("Content_" + uniqueSuffix())
                .user(user)
                .build();
    }

    public static UserCreateRequest validCreateRequest() {
        return new UserCreateRequest("Test User", "test_" + uniqueSuffix() + "@example.com");
    }

    public static Pageable pageable(int page, int size) {
        return PageRequest.of(page, size);
    }
}