package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests for CustomerDao.
 * Uses an in-memory H2 database to ensure test isolation and reproducibility.
 */
@SpringBootTest
@ActiveProfiles("test")
class CustomerDaoTest {

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private org.springframework.jdbc.core.JdbcTemplate jdbcTemplate;

    /**
     * Cleans the customer table before each test to ensure state isolation.
     */
    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DELETE FROM customer");
        // Optional: reset identity counter for predictable IDs in H2
        try {
            jdbcTemplate.execute("ALTER TABLE customer ALTER COLUMN id RESTART WITH 1");
        } catch (Exception e) {
            // Silently ignore if dialect doesn't support this syntax (fallback to auto-increment)
        }
    }

    @Test
    void shouldCreateAndRetrieveCustomer() {
        // Arrange
        Customer newCustomer = new Customer("John Doe", "john.doe@example.com", "123-45-6789");

        // Act
        customerDao.create(newCustomer);
        Customer retrieved = customerDao.findById(1L);

        // Assert
        assertNotNull(retrieved, "Customer should be saved and retrievable by ID");
        assertEquals("John Doe", retrieved.getFullName());
        assertEquals("john.doe@example.com", retrieved.getEmail());
        assertEquals("123-45-6789", retrieved.getSocialSecurityNumber());
        assertEquals(1L, retrieved.getId());
    }

    @Test
    void shouldReturnNullWhenCustomerNotFound() {
        // Act
        Customer notFound = customerDao.findById(999L);

        // Assert
        assertNull(notFound, "findById should return null for non-existent ID");
    }

    @Test
    void shouldUpdateCustomerFields() {
        // Arrange
        customerDao.create(new Customer("Alice", "alice@old.com", "987-65-4321"));
        Customer existing = customerDao.findById(1L);

        // Act
        existing.setFullName("Alice Updated");
        existing.setEmail("alice@new.com");
        customerDao.update(existing);

        // Assert
        Customer updated = customerDao.findById(1L);
        assertNotNull(updated);
        assertEquals("Alice Updated", updated.getFullName());
        assertEquals("alice@new.com", updated.getEmail());
        assertEquals("987-65-4321", updated.getSocialSecurityNumber(), "SSN should remain unchanged");
    }

    @Test
    void shouldDeleteCustomerSuccessfully() {
        // Arrange
        customerDao.create(new Customer("ToDelete", "delete@test.com", "000-00-0000"));
        assertEquals(1, customerDao.findAll().size());

        // Act
        customerDao.delete(1L);

        // Assert
        assertNull(customerDao.findById(1L));
        assertTrue(customerDao.findAll().isEmpty(), "Table should be empty after deletion");
    }

    @Test
    void shouldReturnAllCustomers() {
        // Arrange
        customerDao.create(new Customer("Customer A", "a@test.com", "111-11-1111"));
        customerDao.create(new Customer("Customer B", "b@test.com", "222-22-2222"));

        // Act
        List<Customer> allCustomers = customerDao.findAll();

        // Assert
        assertEquals(2, allCustomers.size());
        assertTrue(allCustomers.stream().anyMatch(c -> c.getFullName().equals("Customer A")));
        assertTrue(allCustomers.stream().anyMatch(c -> c.getFullName().equals("Customer B")));
    }
}