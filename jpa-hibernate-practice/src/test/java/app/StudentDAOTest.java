package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import app.Homework;
import app.Student;
import app.StudentDao;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Test class for StudentDao implementation.
 * All tests verify CRUD operations and bidirectional relationship between Student and Homework.
 */
class StudentDAOTest {

    private static StudentDao studentDAO;
    private static EntityManagerFactory emf;

    @BeforeAll
    public static void setUpBeforeClass() {
        emf = Persistence.createEntityManagerFactory("hillel-persistence-unit");
        studentDAO = new StudentDao(emf);
    }

    @AfterEach
    public void cleanDB() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNativeQuery("TRUNCATE TABLE homework CASCADE").executeUpdate();
            em.createNativeQuery("TRUNCATE TABLE student CASCADE").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE IF EXISTS student_id_seq RESTART WITH 1").executeUpdate();
            em.createNativeQuery("ALTER SEQUENCE IF EXISTS homework_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    @AfterAll
    public static void tearDown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Test
    void save() {
        Student student = getStudent("Alex");
        studentDAO.save(student);
        assertEquals(1, studentDAO.findAll().size());
    }

    @Test
    void findById() {
        Student student = getStudent("Alex");
        studentDAO.save(student);
        Student result = studentDAO.findById(student.getId());
        assertEquals("Alex", result.getFirstName());
        assertEquals("Alex Johnson", result.getFullName());
        assertEquals(student.getId(), result.getId());
    }

    @Test
    void findByEmailNative() {
        Student student = getStudent("Alex");
        studentDAO.save(student);
        Student result = studentDAO.findByEmail(student.getEmail());
        assertEquals("Alex", result.getFirstName());
    }

    @Test
    void findAll() {
        Student student1 = getStudent("Alex");
        Student student2 = getStudent("Oleksandr");
        studentDAO.save(student1);
        studentDAO.save(student2);
        List<Student> st = studentDAO.findAll();
        assertEquals(2, st.size());
        st.forEach(s -> assertTrue(List.of("Alex", "Oleksandr").contains(s.getFirstName())));
    }

    @Test
    void update() {
        Student student = getStudent("Alex");
        studentDAO.save(student);
        student.setFirstName("Tom");
        studentDAO.update(student);
        Student result = studentDAO.findById(student.getId());
        assertEquals("Tom", result.getFirstName());
    }

    @Test
    void deleteById() {
        Student student = getStudent("Alex");
        studentDAO.save(student);
        assertEquals(1, studentDAO.findAll().size());
        studentDAO.deleteById(student.getId());
        assertEquals(0, studentDAO.findAll().size());
    }

    /**
     * Tests bidirectional OneToMany relationship.
     * We use JOIN FETCH to eagerly load homeworks inside the transaction.
     */
    @Test
    void testOneToManyMapping() {
        Student student = getStudent("Alex");
        Homework hw1 = new Homework("Math homework", LocalDate.now().plusDays(5), 10);
        Homework hw2 = new Homework("History homework", LocalDate.now().plusDays(7), 8);

        student.addHomework(hw1);
        student.addHomework(hw2);
        studentDAO.save(student);

        Student studentDB = studentDAO.findByIdWithHomeworks(student.getId());
        assertEquals(2, studentDB.getHomeworks().size(), "Student should have 2 homeworks");
    }

    /**
     * Tests access to student from homework side (lazy proxy).
     * We use JOIN FETCH on both sides.
     */
    @Test
    void getHomeworksById() {
        Student student = getStudent("Alex");
        Homework hw1 = new Homework("Math homework", LocalDate.now().plusDays(5), 10);
        Homework hw2 = new Homework("History homework", LocalDate.now().plusDays(7), 8);

        student.addHomework(hw1);
        student.addHomework(hw2);
        studentDAO.save(student);

        Homework hw = studentDAO.hwFindByIdWithStudent(student.getId());
        assertEquals(2, hw.getStudent().getHomeworks().size(), "Homework should have access to 2 homeworks via student");
    }

    private Student getStudent(String name) {
        return new Student(name, "Johnson", name.toLowerCase() + "@gmail.com");
    }

    private Homework getHomework(String description) {
        return new Homework(description, LocalDate.now(), 5);
    }
}