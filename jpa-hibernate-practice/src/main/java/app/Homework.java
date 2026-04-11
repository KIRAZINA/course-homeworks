package app;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Entity representing a homework assignment.
 * Has a many-to-one relationship with Student.
 */
@Entity
@Table(name = "homework")
public class Homework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "deadline", nullable = false)
    private LocalDate deadline;

    @Column(name = "mark")
    private Integer mark;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    // No-arg constructor required by JPA/Hibernate
    public Homework() {
    }

    // Constructor used in tests
    public Homework(String description, LocalDate deadline, Integer mark) {
        this.description = description;
        this.deadline = deadline;
        this.mark = mark;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDeadline() { return deadline; }
    public void setDeadline(LocalDate deadline) { this.deadline = deadline; }

    public Integer getMark() { return mark; }
    public void setMark(Integer mark) { this.mark = mark; }

    public Student getStudent() { return student; }
    public void setStudent(Student student) { this.student = student; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Homework)) return false;
        Homework homework = (Homework) o;
        return Objects.equals(description, homework.description) &&
                Objects.equals(deadline, homework.deadline) &&
                Objects.equals(mark, homework.mark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, deadline, mark);
    }

    @Override
    public String toString() {
        return "Homework{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", mark=" + mark +
                '}';
    }
}