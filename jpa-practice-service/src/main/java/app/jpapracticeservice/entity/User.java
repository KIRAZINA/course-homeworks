package app.jpapracticeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "posts")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @Builder.Default
    private List<Post> posts = new ArrayList<>();

    public void addPost(Post post) {
        posts.add(post);
        post.setUser(this);
    }

    public void removePost(Post post) {
        posts.remove(post);
        post.setUser(null);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User that)) return false;
        if (id == null || that.id == null) {
            return Objects.equals(email, that.email);
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(getClass(), id);
        }
        return Objects.hash(getClass(), email);
    }
}