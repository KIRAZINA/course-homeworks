package app;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * In-memory storage for products using Map for O(1) lookups.
 * Initialized at application startup. Provides full CRUD operations.
 * Enforces entity identity: no duplicate IDs, update preserves original ID.
 */
@Component
public class ProductRepository {
    private final Map<Long, Product> products = new LinkedHashMap<>();

    /**
     * Pre-populates the repository with sample data on startup.
     */
    @PostConstruct
    public void initData() {
        create(new Product(1L, "Gaming Laptop", new BigDecimal("1299.99")));
        create(new Product(2L, "Wireless Mouse", new BigDecimal("29.99")));
        create(new Product(3L, "Mechanical Keyboard", new BigDecimal("89.50")));
        create(new Product(4L, "27-inch Monitor", new BigDecimal("349.00")));
        create(new Product(5L, "USB-C Hub", new BigDecimal("45.00")));
    }

    // READ operations
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    // CREATE operation - rejects duplicate IDs
    public Product create(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null");
        }
        if (products.containsKey(product.getId())) {
            throw new IllegalStateException(
                    "Product with ID " + product.getId() + " already exists");
        }
        products.put(product.getId(), product);
        return product;
    }

    // UPDATE operation - preserves original ID, rejects mismatched IDs
    public Optional<Product> update(Long id, Product updatedProduct) {
        if (id == null || updatedProduct == null) {
            throw new IllegalArgumentException("ID and updated product cannot be null");
        }
        if (!id.equals(updatedProduct.getId())) {
            throw new IllegalArgumentException(
                    "Update ID (" + id + ") must match product ID (" + updatedProduct.getId() + ")");
        }
        if (!products.containsKey(id)) {
            return Optional.empty();
        }
        products.put(id, updatedProduct);
        return Optional.of(updatedProduct);
    }

    // DELETE operation
    public boolean delete(Long id) {
        return products.remove(id) != null;
    }
}