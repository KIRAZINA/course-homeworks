package app;

import java.util.Objects;

public class User {
    private final int id;
    private final String name;
    private final String email;

    // Constructor
    public User(int id, String name, String email) {
        if (name == null || email == null || email.isBlank()) {
            throw new IllegalArgumentException("Name and email must not be null or empty");
        }
        this.id = id;
        this.name = name;
        this.email = email.toLowerCase();
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', email='" + email + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}
