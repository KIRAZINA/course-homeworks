package app.repository;

import app.model.Order;
import app.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Plain unit tests for OrderRepository - no Spring context needed.
 */
class OrderRepositoryTest {

    private OrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new OrderRepository();
        repository.clear();
    }

    @Test
    @DisplayName("save assigns auto-generated ID")
    void save_assignsId() {
        Order order = new Order(List.of(new Product("Laptop", BigDecimal.valueOf(999.99))));
        Order saved = repository.save(order);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById returns saved order")
    void findById_returnsOrder() {
        Order order = new Order(List.of(new Product("Mouse", BigDecimal.valueOf(29.99))));
        Order saved = repository.save(order);

        Optional<Order> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(saved.getId());
    }

    @Test
    @DisplayName("findById returns empty for unknown ID")
    void findById_returnsEmpty() {
        assertThat(repository.findById(999L)).isEmpty();
    }

    @Test
    @DisplayName("findAll returns all orders sorted by ID")
    void findAll_returnsSorted() {
        repository.save(new Order(List.of(new Product("C", BigDecimal.valueOf(3)))));
        repository.save(new Order(List.of(new Product("A", BigDecimal.ONE))));

        List<Order> orders = repository.findAll();

        assertThat(orders).hasSize(2);
        assertThat(orders).extracting(Order::getId).containsExactly(1L, 2L);
    }

    @Test
    @DisplayName("totalCost calculated with BigDecimal precision")
    void totalCost_bigDecimalPrecision() {
        Product p1 = new Product("A", new BigDecimal("0.1"));
        Product p2 = new Product("B", new BigDecimal("0.2"));
        Order order = new Order(List.of(p1, p2));

        assertThat(order.getTotalCost()).isEqualTo(new BigDecimal("0.3"));
    }
}