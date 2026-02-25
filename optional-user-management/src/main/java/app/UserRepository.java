package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final List<User> users = new ArrayList<>();

    // Add user to repository
    public void addUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User must not be null");
        }
        users.add(user);
    }

    // Find user by ID
    public Optional<User> findUserById(int id) {
        return users.stream()
                .filter(user -> user.getId() == id)
                .findFirst();
    }

    // Find user by email
    public Optional<User> findUserByEmail(String email) {
        return Optional.ofNullable(email)
                .filter(s -> !s.isBlank())
                .map(String::trim)
                .map(String::toLowerCase)
                .flatMap(normalized ->
                        users.stream()
                                .filter(user -> user.getEmail().equals(normalized))
                                .findFirst()
                );
    }

    // Find all users
    public List<User> findAllUsers() {
        return List.copyOf(users); // defensive copy
    }
}
