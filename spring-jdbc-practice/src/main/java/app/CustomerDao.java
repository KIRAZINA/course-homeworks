package app;

import java.util.List;

/**
 * Data Access Object interface for Customer entity.
 * Defines standard CRUD operations.
 */
public interface CustomerDao {
    void create(Customer customer);
    Customer findById(Long id);
    void update(Customer customer);
    void delete(Long id);
    List<Customer> findAll();
}