package app;

import jakarta.persistence.*;
import java.util.List;

public class StudentDao implements GenericDao<Student, Long> {

    private final EntityManagerFactory emf;

    public StudentDao(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Student entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Student findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.find(Student.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public Student findByEmail(String email) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Student> query = em.createQuery(
                    "SELECT s FROM Student s WHERE s.email = :email", Student.class);
            query.setParameter("email", email);
            return query.getResultStream().findFirst().orElse(null);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Student> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT s FROM Student s", Student.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public Student update(Student entity) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Student updated = em.merge(entity);
            em.getTransaction().commit();
            return updated;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public boolean deleteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Student student = em.find(Student.class, id);
            if (student != null) {
                em.remove(student);
                em.getTransaction().commit();
                return true;
            } else {
                em.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    /**
     * Finds Student by id with all homeworks eagerly loaded.
     */
    public Student findByIdWithHomeworks(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT s FROM Student s LEFT JOIN FETCH s.homeworks WHERE s.id = :id",
                            Student.class)
                    .setParameter("id", id)
                    .getSingleResult();
        } finally {
            em.close();
        }
    }

    /**
     * Finds one Homework of the student with Student and all homeworks eagerly loaded.
     */
    public Homework hwFindByIdWithStudent(Long studentId) {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery(
                            "SELECT h FROM Homework h " +
                                    "JOIN FETCH h.student s " +
                                    "LEFT JOIN FETCH s.homeworks " +
                                    "WHERE h.student.id = :studentId", Homework.class)
                    .setParameter("studentId", studentId)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            em.close();
        }
    }

    public StudentDao() {
        this.emf = Persistence.createEntityManagerFactory("hillel-persistence-unit");
    }
}
