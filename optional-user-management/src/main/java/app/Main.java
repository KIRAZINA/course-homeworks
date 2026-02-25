package app;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        UserRepository repository = new UserRepository();

        // Add sample users
        repository.addUser(new User(1, "Alice", "alice@example.com"));
        repository.addUser(new User(2, "Bob", "bob@example.com"));
        repository.addUser(new User(3, "Charlie", "charlie@example.com"));

        // Scenario A: Found
        repository.findUserById(2).ifPresentOrElse(
                user -> System.out.println("Found by ID: " + user),
                () -> System.out.println("User not found by ID")
        );

        // Scenario A: Not found
        repository.findUserById(999).ifPresentOrElse(
                user -> System.out.println("Found by ID: " + user),
                () -> System.out.println("User not found by ID (999)")
        );

        // Scenario B: Found
        repository.findUserByEmail("alice@example.com").ifPresentOrElse(
                user -> System.out.println("Found by Email: " + user),
                () -> System.out.println("User not found by Email")
        );

        // Scenario B: Not found
        repository.findUserByEmail("nobody@example.com").ifPresentOrElse(
                user -> System.out.println("Found by Email: " + user),
                () -> System.out.println("User not found by Email (nobody@example.com)")
        );

        // Scenario C: All users
        List<User> allUsers = repository.findAllUsers();
        System.out.println("Total users: " + allUsers.size());
    }
}
