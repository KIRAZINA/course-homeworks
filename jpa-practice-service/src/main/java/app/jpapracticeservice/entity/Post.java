package app.jpapracticeservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "posts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(exclude = "user")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false, length = 255)
    private String title;

    @NotBlank(message = "Content is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_post_user"))
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post that)) return false;
        if (id == null || that.id == null) {
            return Objects.equals(title, that.title) && Objects.equals(content, that.content);
        }
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return Objects.hash(getClass(), id);
        }
        return Objects.hash(getClass(), title, content);
    }
}