package app;

import jakarta.persistence.*;
import java.util.*;

/**
 * Entity representing a student in the system.
 * Has a one-to-many relationship with Homework.
 */
@Entity
@Table(name = "student")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Homework> homeworks = new HashSet<>();

    public Student() {
    }

    public Student(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Homework> getHomeworks() {
        return Collections.unmodifiableSet(homeworks);
    }

    public void setHomeworks(Set<Homework> homeworks) {
        this.homeworks.clear();
        if (homeworks != null) {
            for (Homework hw : homeworks) {
                addHomework(hw);
            }
        }
    }

    /**
     * Adds a homework and maintains bidirectional relationship.
     */
    public void addHomework(final Homework homework) {
        if (homework != null) {
            homeworks.removeIf(h ->
                    h.getDescription().equals(homework.getDescription()) &&
                            Objects.equals(h.getDeadline(), homework.getDeadline()));

            homeworks.add(homework);
            homework.setStudent(this);
        }
    }

    /**
     * Removes a homework and maintains bidirectional relationship.
     */
    public void removeHomework(final Homework homework) {
        if (homework != null) {
            homeworks.remove(homework);
            homework.setStudent(null);
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}