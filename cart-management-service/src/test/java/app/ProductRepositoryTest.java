package app;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

/**
 * Unit tests for ProductRepository.
 * Tests in-memory CRUD operations and identity constraints.
 */
@DisplayName("ProductRepository in-memory operations")
class ProductRepositoryTest {

    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        // Create fresh repository for each test (no Spring context needed)
        repository = new ProductRepository();
        // Manually initialize to avoid @PostConstruct in unit tests
        repository.initData();
    }

    @Nested
    @DisplayName("Create operation")
    class CreateTests {

        @Test
        @DisplayName("should reject duplicate IDs")
        void shouldRejectDuplicateIds() {
            Product existing = repository.findById(1L).orElseThrow();

            Product duplicate = new Product(1L, "Duplicate", BigDecimal.TEN);

            assertThatThrownBy(() -> repository.create(duplicate))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("already exists");
        }

        @Test
        @DisplayName("should reject null product")
        void shouldRejectNullProduct() {
            assertThatThrownBy(() -> repository.create(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Product cannot be null");
        }

        @Test
        @DisplayName("should create new product successfully")
        void shouldCreateProduct() {
            Product newProduct = new Product(100L, "New Item", new BigDecimal("49.99"));

            Product created = repository.create(newProduct);

            assertThat(created).isSameAs(newProduct);
            assertThat(repository.findById(100L)).isPresent();
        }
    }

    @Nested
    @DisplayName("Read operations")
    class ReadTests {

        @Test
        @DisplayName("findById should return existing product")
        void shouldFindExistingProduct() {
            Optional<Product> result = repository.findById(1L);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Gaming Laptop");
        }

        @Test
        @DisplayName("findById should return empty for unknown ID")
        void shouldReturnEmptyForUnknownId() {
            Optional<Product> result = repository.findById(999L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("findAll should return all products")
        void shouldReturnAllProducts() {
            List<Product> products = repository.findAll();

            assertThat(products).hasSize(5);
            assertThat(products).extracting(Product::getId)
                    .containsExactlyInAnyOrder(1L, 2L, 3L, 4L, 5L);
        }

        @Test
        @DisplayName("findAll should return defensive copy")
        void findAllShouldReturnDefensiveCopy() {
            List<Product> products = repository.findAll();
            int originalSize = products.size();

            products.add(new Product(999L, "Fake", BigDecimal.ONE));

            assertThat(repository.findAll()).hasSize(originalSize);
        }
    }

    @Nested
    @DisplayName("Update operation")
    class UpdateTests {

        @Test
        @DisplayName("should reject mismatched IDs during update")
        void shouldRejectMismatchedIds() {
            Product updated = new Product(999L, "Changed", BigDecimal.ONE);

            assertThatThrownBy(() -> repository.update(1L, updated))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("must match");
        }

        @Test
        @DisplayName("should reject null ID in update")
        void shouldRejectNullIdInUpdate() {
            Product product = repository.findById(1L).orElseThrow();

            assertThatThrownBy(() -> repository.update(null, product))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should reject null product in update")
        void shouldRejectNullProductInUpdate() {
            assertThatThrownBy(() -> repository.update(1L, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("should return empty when updating non-existent ID")
        void shouldReturnEmptyForNonExistentId() {
            Product product = new Product(999L, "Ghost", BigDecimal.TEN);

            Optional<Product> result = repository.update(999L, product);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("should update existing product")
        void shouldUpdateProduct() {
            Product updated = new Product(1L, "Updated Laptop", new BigDecimal("1199.99"));

            Optional<Product> result = repository.update(1L, updated);

            assertThat(result).isPresent();
            assertThat(result.get().getName()).isEqualTo("Updated Laptop");
            assertThat(result.get().getPrice()).isEqualByComparingTo("1199.99");
        }
    }

    @Nested
    @DisplayName("Delete operation")
    class DeleteTests {

        @Test
        @DisplayName("should delete existing product")
        void shouldDeleteExistingProduct() {
            boolean result = repository.delete(1L);

            assertThat(result).isTrue();
            assertThat(repository.findById(1L)).isEmpty();
        }

        @Test
        @DisplayName("should return false when deleting non-existent ID")
        void shouldReturnFalseForNonExistentId() {
            boolean result = repository.delete(999L);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("delete should be idempotent")
        void deleteShouldBeIdempotent() {
            repository.delete(1L);
            boolean secondDelete = repository.delete(1L);

            assertThat(secondDelete).isFalse();
        }
    }
}